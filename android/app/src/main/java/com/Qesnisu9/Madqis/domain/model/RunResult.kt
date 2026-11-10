package com.Qesnisu9.Madqis.domain.model

data class RunResult(
    val score: Int,
    val treats: Int,
    val bestCombo: Int,
    val segment: Int,
    val completed: Boolean,
    val coinsEarned: Int,
    val newBest: Boolean,
    val routeTitle: String
)
