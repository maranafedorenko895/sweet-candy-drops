package com.Qesnisu9.Madqis.domain.usecase

import com.Qesnisu9.Madqis.data.sample.SampleData
import com.Qesnisu9.Madqis.domain.model.RunResult
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository

class SaveRunUseCase(private val progressRepository: ProgressRepository) {

    operator fun invoke(
        score: Int,
        treats: Int,
        bestCombo: Int,
        segment: Int,
        completed: Boolean,
        routeTitle: String,
        dailyTarget: Int,
        dayOfYear: Int
    ): RunResult {
        val stored = progressRepository.load()
        val newBest = score > stored.bestScore
        val coins = if (completed) BASE_COINS + segment else segment
        val treatsTotal = stored.treatsCollected + treats
        val unlocked = SampleData.candyCollection
            .filter { treatsTotal >= it.treatThreshold }
            .map { it.id }
            .toSet()
        val dailyDone = dailyTarget > 0 && score >= dailyTarget
        progressRepository.save(
            stored.copy(
                bestScore = maxOf(stored.bestScore, score),
                totalScore = stored.totalScore + score,
                routesFinished = stored.routesFinished + if (completed) 1 else 0,
                treatsCollected = treatsTotal,
                bestCombo = maxOf(stored.bestCombo, bestCombo),
                bestSegment = maxOf(stored.bestSegment, segment),
                sugarCoins = stored.sugarCoins + coins,
                unlockedCandies = stored.unlockedCandies + unlocked,
                dailyClaimedDayOfYear = if (dailyDone) dayOfYear else stored.dailyClaimedDayOfYear
            )
        )
        return RunResult(
            score = score,
            treats = treats,
            bestCombo = bestCombo,
            segment = segment,
            completed = completed,
            coinsEarned = coins,
            newBest = newBest,
            routeTitle = routeTitle
        )
    }

    private companion object {
        const val BASE_COINS = 5
    }
}
