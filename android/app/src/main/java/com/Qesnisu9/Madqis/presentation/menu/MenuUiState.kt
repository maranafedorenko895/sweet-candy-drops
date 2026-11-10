package com.Qesnisu9.Madqis.presentation.menu

data class MenuUiState(
    val loading: Boolean = true,
    val bestScore: Int = 0,
    val routesFinished: Int = 0,
    val treats: Int = 0,
    val sugarCoins: Int = 0,
    val dailyTitle: String = "",
    val dailyTarget: Int = 0,
    val dailyClaimed: Boolean = false,
    val dailyProgress: Float = 0f,
    val reducedMotion: Boolean = false
) {
    val hasStats: Boolean get() = bestScore > 0 && routesFinished > 0
}
