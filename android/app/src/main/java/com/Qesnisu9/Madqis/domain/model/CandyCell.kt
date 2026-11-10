package com.Qesnisu9.Madqis.domain.model

enum class CandyCell {
    EMPTY,
    TREAT,
    BONUS,
    HAZARD,
    BOOSTER
}

data class BoardRow(
    val id: Long,
    val cells: List<CandyCell>
)
