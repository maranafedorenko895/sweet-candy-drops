package com.Qesnisu9.Madqis.presentation.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository
import com.Qesnisu9.Madqis.domain.usecase.EvaluateDailyRouteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyChallengeViewModel(
    private val progressRepository: ProgressRepository,
    private val evaluateDailyRoute: EvaluateDailyRouteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DailyChallengeUiState())
    val state: StateFlow<DailyChallengeUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val daily = evaluateDailyRoute(progress)
            val ratio = if (daily.target > 0) {
                (progress.bestScore.toFloat() / daily.target.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
            _state.update {
                it.copy(
                    loading = false,
                    available = daily.title.isNotBlank() && daily.target > 0,
                    title = daily.title,
                    target = daily.target,
                    bestScore = progress.bestScore,
                    progress = if (daily.claimed) 1f else ratio,
                    claimed = daily.claimed,
                    seed = daily.seed,
                    todayIndex = daily.dayOfWeek
                )
            }
        }
    }
}
