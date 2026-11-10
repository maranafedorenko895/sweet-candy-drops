package com.Qesnisu9.Madqis.domain.model

data class DailyRoute(
    val title: String,
    val target: Int,
    val seed: Int,
    val dayOfYear: Int,
    val dayOfWeek: Int,
    val claimed: Boolean
)
