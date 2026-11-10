package com.Qesnisu9.Madqis.domain.model

data class Reward(
    val id: Int,
    val title: String,
    val requiredRoutes: Int,
    val coins: Int
)

enum class RewardState {
    LOCKED,
    UNLOCKED,
    CLAIMED
}
