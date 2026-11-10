package com.Qesnisu9.Madqis.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val READY_GRACE_MS = 10000L
    const val IDLE_FINISH_MS = 12000L
    const val MIN_RUN_MS = 58000L
    const val LANES = 3
    const val MAX_LIVES = 3
    const val SEGMENTS = 12
    const val STEPS_PER_SEGMENT = 10
    const val TOTAL_STEPS = SEGMENTS * STEPS_PER_SEGMENT
    const val STEP_START_MS = 520L
    const val STEP_MIN_MS = 260L
    const val STEP_DECAY_MS = 18L
    const val ROW_CAPACITY = 9
    const val BOOSTER_STEPS = 5
    const val BOOSTER_MULTIPLIER = 3
    const val MAX_MULTIPLIER = 5
    const val COMBO_PER_LEVEL = 5
    const val EMPTY_DECAY_STEPS = 3
    const val TREAT_POINTS = 10
    const val BONUS_POINTS = 25
    const val COMBO_CHIP_AT = 5
    const val COLLECTION_SIZE = 8
}
