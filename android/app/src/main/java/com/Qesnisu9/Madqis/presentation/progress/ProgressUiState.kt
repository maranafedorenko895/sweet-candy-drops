package com.Qesnisu9.Madqis.presentation.progress

data class ProgressUiState(
    val loading: Boolean = true,
    val bestScore: Int = 0,
    val totalScore: Int = 0,
    val routesFinished: Int = 0,
    val treatsCollected: Int = 0,
    val bestCombo: Int = 0,
    val bestSegment: Int = 0,
    val sugarCoins: Int = 0
) {
    val isEmpty: Boolean get() = totalScore == 0 && treatsCollected == 0
}
