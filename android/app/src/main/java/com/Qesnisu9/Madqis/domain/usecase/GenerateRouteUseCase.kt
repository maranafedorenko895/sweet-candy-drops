package com.Qesnisu9.Madqis.domain.usecase

import com.Qesnisu9.Madqis.core.config.GameConfig
import com.Qesnisu9.Madqis.domain.model.BoardRow
import com.Qesnisu9.Madqis.domain.model.CandyCell
import com.Qesnisu9.Madqis.domain.model.Lane
import com.Qesnisu9.Madqis.domain.model.RouteStep
import kotlin.random.Random

class GenerateRouteUseCase {

    operator fun invoke(index: Int, random: Random): RouteStep {
        val segment = segmentOf(index)
        val progress = (segment - 1).toFloat() / (GameConfig.SEGMENTS - 1).toFloat()
        val hazardChance = HAZARD_START + (HAZARD_END - HAZARD_START) * progress
        val treatChance = TREAT_START + (TREAT_END - TREAT_START) * progress
        val cells = MutableList(Lane.COUNT) { CandyCell.EMPTY }
        if (index > WARMUP_STEPS && random.nextFloat() < hazardChance) {
            cells[random.nextInt(Lane.COUNT)] = CandyCell.HAZARD
        }
        for (lane in 0 until Lane.COUNT) {
            if (cells[lane] != CandyCell.EMPTY) continue
            val roll = random.nextFloat()
            cells[lane] = when {
                roll < treatChance -> CandyCell.TREAT
                roll < treatChance + BONUS_CHANCE -> CandyCell.BONUS
                roll < treatChance + BONUS_CHANCE + BOOSTER_CHANCE -> CandyCell.BOOSTER
                else -> CandyCell.EMPTY
            }
        }
        return RouteStep(
            index = index,
            segment = segment,
            stepMs = stepMsOf(segment),
            row = BoardRow(id = index.toLong(), cells = cells)
        )
    }

    fun segmentOf(index: Int): Int =
        (index / GameConfig.STEPS_PER_SEGMENT + 1).coerceIn(1, GameConfig.SEGMENTS)

    fun stepMsOf(segment: Int): Long =
        (GameConfig.STEP_START_MS - GameConfig.STEP_DECAY_MS * (segment - 1))
            .coerceAtLeast(GameConfig.STEP_MIN_MS)

    private companion object {
        const val HAZARD_START = 0.12f
        const val HAZARD_END = 0.34f
        const val TREAT_START = 0.46f
        const val TREAT_END = 0.28f
        const val BONUS_CHANCE = 0.07f
        const val BOOSTER_CHANCE = 0.05f
        const val WARMUP_STEPS = 3
    }
}
