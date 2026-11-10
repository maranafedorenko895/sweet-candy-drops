package com.Qesnisu9.Madqis.domain.model

enum class CandyRarity {
    COMMON,
    SWEET,
    RARE
}

data class CandyCollectible(
    val id: Int,
    val title: String,
    val rarity: CandyRarity,
    val treatThreshold: Int
)
