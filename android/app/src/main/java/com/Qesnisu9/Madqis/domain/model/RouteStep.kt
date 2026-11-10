package com.Qesnisu9.Madqis.domain.model

data class RouteStep(
    val index: Int,
    val segment: Int,
    val stepMs: Long,
    val row: BoardRow
)

data class StepResolution(
    val points: Int,
    val treats: Int,
    val combo: Int,
    val lives: Int,
    val boosterSteps: Int,
    val multiplier: Int,
    val hazardHit: Boolean
)
