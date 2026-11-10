package com.Qesnisu9.Madqis.presentation.gameover

data class GameOverUiState(
    val loading: Boolean = true,
    val bestScore: Int = 0,
    val routesFinished: Int = 0,
    val sugarCoins: Int = 0
)
