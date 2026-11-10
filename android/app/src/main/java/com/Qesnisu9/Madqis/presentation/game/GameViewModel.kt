package com.Qesnisu9.Madqis.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qesnisu9.Madqis.core.config.GameConfig
import com.Qesnisu9.Madqis.data.sample.SampleData
import com.Qesnisu9.Madqis.domain.model.CandyCell
import com.Qesnisu9.Madqis.domain.model.Lane
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository
import com.Qesnisu9.Madqis.domain.repository.SettingsRepository
import com.Qesnisu9.Madqis.domain.usecase.EvaluateDailyRouteUseCase
import com.Qesnisu9.Madqis.domain.usecase.GenerateRouteUseCase
import com.Qesnisu9.Madqis.domain.usecase.ResolveStepUseCase
import com.Qesnisu9.Madqis.domain.usecase.SaveRunUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val evaluateDailyRoute: EvaluateDailyRouteUseCase,
    private val generateRoute: GenerateRouteUseCase,
    private val resolveStep: ResolveStepUseCase,
    private val saveRun: SaveRunUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    private var loopJob: Job? = null
    private var graceJob: Job? = null
    private var idleJob: Job? = null
    private var flashJob: Job? = null
    private var random: Random = Random(DEFAULT_SEED)
    private var boosterSteps = 0
    private var emptyStreak = 0
    private var dailyTarget = 0
    private var dayOfYear = 0
    private var routeTitle = ""
    private var preparedToken = 0L
    private var enteredAt = 0L

    fun prepare(dailySeed: Int?, token: Long) {
        if (preparedToken == token) return
        preparedToken = token
        cancelJobs()
        val progress = progressRepository.load()
        val settings = settingsRepository.load()
        val daily = evaluateDailyRoute(progress)
        dailyTarget = if (dailySeed != null) daily.target else 0
        dayOfYear = daily.dayOfYear
        routeTitle = if (dailySeed != null) daily.title else SampleData.segmentTitles.first()
        random = Random(dailySeed?.toLong() ?: System.nanoTime())
        boosterSteps = 0
        emptyStreak = 0
        enteredAt = System.currentTimeMillis()
        _state.value = GameUiState(
            phase = GamePhase.READY,
            rows = List(GameConfig.ROW_CAPACITY) { index ->
                generateRoute(GameConfig.ROW_CAPACITY - index, random).row
            },
            routeTitle = routeTitle,
            bigCandyMode = settings.bigCandyMode,
            reducedMotion = settings.reducedMotion
        )
        graceJob = viewModelScope.launch {
            delay(GameConfig.READY_GRACE_MS)
            if (_state.value.phase == GamePhase.READY) begin()
        }
    }

    fun moveTo(lane: Int) {
        if (_state.value.phase == GamePhase.FINISHED) return
        val target = Lane.clamp(lane)
        _state.update { it.copy(heroLane = target) }
        if (_state.value.phase == GamePhase.READY) {
            begin()
        } else {
            armIdle()
        }
    }

    fun moveBy(delta: Int) {
        moveTo(_state.value.heroLane + delta)
    }

    fun openPause() {
        if (_state.value.phase == GamePhase.FINISHED) return
        loopJob?.cancel()
        idleJob?.cancel()
        _state.update { it.copy(phase = GamePhase.PAUSED, pauseVisible = true) }
    }

    fun dismissPause() {
        if (_state.value.phase != GamePhase.PAUSED) return
        _state.update { it.copy(phase = GamePhase.RUNNING, pauseVisible = false) }
        startLoop()
        armIdle()
    }

    fun leaveToMenu() {
        cancelJobs()
        _state.update { it.copy(pauseVisible = false) }
        viewModelScope.launch { _events.send(GameEvent.ExitToMenu) }
    }

    fun pause() {
        if (_state.value.phase != GamePhase.RUNNING) return
        loopJob?.cancel()
        idleJob?.cancel()
        _state.update { it.copy(phase = GamePhase.PAUSED) }
    }

    private fun begin() {
        if (_state.value.phase == GamePhase.RUNNING) return
        graceJob?.cancel()
        _state.update { it.copy(phase = GamePhase.RUNNING, pauseVisible = false) }
        startLoop()
        armIdle()
    }

    private fun startLoop() {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            while (isActive) {
                delay(_state.value.stepMs)
                if (_state.value.phase != GamePhase.RUNNING) break
                advance()
            }
        }
    }

    private fun armIdle() {
        if (_state.value.phase != GamePhase.RUNNING) return
        idleJob?.cancel()
        val sinceEntry = System.currentTimeMillis() - enteredAt
        val wait = maxOf(GameConfig.IDLE_FINISH_MS, GameConfig.MIN_RUN_MS - sinceEntry)
        idleJob = viewModelScope.launch {
            delay(wait)
            if (_state.value.phase == GamePhase.RUNNING) finish(false)
        }
    }

    private fun advance() {
        val current = _state.value
        val arriving = current.rows.lastOrNull() ?: return
        val cell = arriving.cells.getOrElse(current.heroLane) { CandyCell.EMPTY }
        val resolution = resolveStep(cell, current.combo, current.lives, boosterSteps)
        boosterSteps = resolution.boosterSteps
        emptyStreak = if (cell == CandyCell.EMPTY) emptyStreak + 1 else 0
        var combo = resolution.combo
        if (emptyStreak >= GameConfig.EMPTY_DECAY_STEPS) {
            combo = (combo - 1).coerceAtLeast(0)
            emptyStreak = 0
        }
        val nextIndex = current.stepIndex + 1
        val step = generateRoute(nextIndex + GameConfig.ROW_CAPACITY, random)
        val segment = generateRoute.segmentOf(nextIndex)
        val score = current.score + resolution.points
        val lives = resolution.lives
        _state.update {
            it.copy(
                rows = listOf(step.row) + it.rows.dropLast(1),
                score = score,
                treats = it.treats + resolution.treats,
                combo = combo,
                bestCombo = maxOf(it.bestCombo, combo),
                multiplier = resolveStep.multiplierOf(combo, boosterSteps),
                lives = lives,
                stepIndex = nextIndex,
                segment = segment,
                stepMs = generateRoute.stepMsOf(segment),
                hazardFlash = resolution.hazardHit,
                pickupPoints = if (resolution.points > 0) resolution.points else 0,
                pickupToken = if (resolution.points > 0) it.pickupToken + 1L else it.pickupToken
            )
        }
        if (resolution.hazardHit) {
            flashJob?.cancel()
            flashJob = viewModelScope.launch {
                delay(FLASH_MS)
                _state.update { it.copy(hazardFlash = false) }
            }
        }
        when {
            lives <= 0 -> finish(false)
            nextIndex >= GameConfig.TOTAL_STEPS -> finish(true)
        }
    }

    private fun finish(completed: Boolean) {
        cancelJobs()
        val snapshot = _state.value
        _state.update { it.copy(phase = GamePhase.FINISHED, pauseVisible = false, hazardFlash = false) }
        preparedToken = 0L
        viewModelScope.launch {
            val result = saveRun(
                score = snapshot.score,
                treats = snapshot.treats,
                bestCombo = snapshot.bestCombo,
                segment = snapshot.segment,
                completed = completed,
                routeTitle = routeTitle,
                dailyTarget = dailyTarget,
                dayOfYear = dayOfYear
            )
            _events.send(GameEvent.Finished(result))
        }
    }

    private fun cancelJobs() {
        loopJob?.cancel()
        graceJob?.cancel()
        idleJob?.cancel()
        flashJob?.cancel()
    }

    override fun onCleared() {
        cancelJobs()
        super.onCleared()
    }

    private companion object {
        const val DEFAULT_SEED = 7L
        const val FLASH_MS = 260L
    }
}
