package com.Qesnisu9.Madqis.domain.model

object Lane {
    const val LEFT = 0
    const val CENTER = 1
    const val RIGHT = 2
    const val COUNT = 3

    fun clamp(lane: Int): Int = lane.coerceIn(LEFT, RIGHT)
}
