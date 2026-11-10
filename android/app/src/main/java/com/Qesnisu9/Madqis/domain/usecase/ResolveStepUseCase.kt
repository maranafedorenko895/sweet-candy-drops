package com.Qesnisu9.Madqis.domain.usecase

import com.Qesnisu9.Madqis.core.config.GameConfig
import com.Qesnisu9.Madqis.domain.model.CandyCell
import com.Qesnisu9.Madqis.domain.model.StepResolution

class ResolveStepUseCase {

    operator fun invoke(
        cell: CandyCell,
        combo: Int,
        lives: Int,
        boosterSteps: Int
    ): StepResolution {
        val multiplier = multiplierOf(combo, boosterSteps)
        val remainingBooster = (boosterSteps - 1).coerceAtLeast(0)
        return when (cell) {
            CandyCell.TREAT -> StepResolution(
                points = GameConfig.TREAT_POINTS * multiplier,
                treats = 1,
                combo = combo + 1,
                lives = lives,
                boosterSteps = remainingBooster,
                multiplier = multiplier,
                hazardHit = false
            )
            CandyCell.BONUS -> StepResolution(
                points = GameConfig.BONUS_POINTS * multiplier,
                treats = 1,
                combo = combo + 1,
                lives = (lives + 1).coerceAtMost(GameConfig.MAX_LIVES),
                boosterSteps = remainingBooster,
                multiplier = multiplier,
                hazardHit = false
            )
            CandyCell.BOOSTER -> StepResolution(
                points = 0,
                treats = 0,
                combo = combo,
                lives = lives,
                boosterSteps = GameConfig.BOOSTER_STEPS,
                multiplier = GameConfig.BOOSTER_MULTIPLIER,
                hazardHit = false
            )
            CandyCell.HAZARD -> StepResolution(
                points = 0,
                treats = 0,
                combo = 0,
                lives = lives - 1,
                boosterSteps = 0,
                multiplier = 1,
                hazardHit = true
            )
            CandyCell.EMPTY -> StepResolution(
                points = 0,
                treats = 0,
                combo = combo,
                lives = lives,
                boosterSteps = remainingBooster,
                multiplier = multiplier,
                hazardHit = false
            )
        }
    }

    fun multiplierOf(combo: Int, boosterSteps: Int): Int = when {
        boosterSteps > 0 -> GameConfig.BOOSTER_MULTIPLIER
        else -> (1 + combo / GameConfig.COMBO_PER_LEVEL).coerceAtMost(GameConfig.MAX_MULTIPLIER)
    }
}
