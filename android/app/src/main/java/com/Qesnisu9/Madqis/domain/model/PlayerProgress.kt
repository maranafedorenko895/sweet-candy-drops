package com.Qesnisu9.Madqis.domain.model

data class PlayerProgress(
    val bestScore: Int = 0,
    val totalScore: Int = 0,
    val routesFinished: Int = 0,
    val treatsCollected: Int = 0,
    val bestCombo: Int = 0,
    val bestSegment: Int = 0,
    val sugarCoins: Int = 0,
    val claimedRewards: Set<Int> = emptySet(),
    val unlockedCandies: Set<Int> = emptySet(),
    val dailyClaimedDayOfYear: Int = -1
)
