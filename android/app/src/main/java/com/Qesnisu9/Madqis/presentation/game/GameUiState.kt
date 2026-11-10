package com.Qesnisu9.Madqis.presentation.game

import com.Qesnisu9.Madqis.core.config.GameConfig
import com.Qesnisu9.Madqis.domain.model.BoardRow
import com.Qesnisu9.Madqis.domain.model.Lane
import com.Qesnisu9.Madqis.domain.model.RunResult

enum class GamePhase {
    READY,
    RUNNING,
    PAUSED,
    FINISHED
}

data class GameUiState(
    val phase: GamePhase = GamePhase.READY,
    val rows: List<BoardRow> = emptyList(),
    val heroLane: Int = Lane.CENTER,
    val score: Int = 0,
    val treats: Int = 0,
    val combo: Int = 0,
    val bestCombo: Int = 0,
    val multiplier: Int = 1,
    val lives: Int = GameConfig.MAX_LIVES,
    val stepIndex: Int = 0,
    val segment: Int = 1,
    val stepMs: Long = GameConfig.STEP_START_MS,
    val hazardFlash: Boolean = false,
    val pickupPoints: Int = 0,
    val pickupToken: Long = 0L,
    val routeTitle: String = "",
    val pauseVisible: Boolean = false,
    val bigCandyMode: Boolean = false,
    val reducedMotion: Boolean = false
) {
    val segmentProgress: Float
        get() = (stepIndex.toFloat() / GameConfig.TOTAL_STEPS.toFloat()).coerceIn(0f, 1f)
}

sealed interface GameEvent {
    data class Finished(val result: RunResult) : GameEvent
    data object ExitToMenu : GameEvent
}
