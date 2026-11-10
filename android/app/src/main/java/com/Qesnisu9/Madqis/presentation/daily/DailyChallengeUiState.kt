package com.Qesnisu9.Madqis.presentation.daily

data class DailyChallengeUiState(
    val loading: Boolean = true,
    val available: Boolean = false,
    val title: String = "",
    val target: Int = 0,
    val bestScore: Int = 0,
    val progress: Float = 0f,
    val claimed: Boolean = false,
    val seed: Int = 0,
    val todayIndex: Int = 0
)
