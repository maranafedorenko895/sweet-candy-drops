package com.Qesnisu9.Madqis.domain.usecase

import com.Qesnisu9.Madqis.data.sample.SampleData
import com.Qesnisu9.Madqis.domain.model.PlayerProgress
import com.Qesnisu9.Madqis.domain.model.RewardState
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository

class ClaimRewardUseCase(private val progressRepository: ProgressRepository) {

    operator fun invoke(rewardId: Int): PlayerProgress {
        val stored = progressRepository.load()
        val reward = SampleData.rewardLadder.firstOrNull { it.id == rewardId } ?: return stored
        if (stateOf(reward.id, reward.requiredRoutes, stored) != RewardState.UNLOCKED) return stored
        val updated = stored.copy(
            sugarCoins = stored.sugarCoins + reward.coins,
            claimedRewards = stored.claimedRewards + reward.id
        )
        progressRepository.save(updated)
        return updated
    }

    fun stateOf(rewardId: Int, requiredRoutes: Int, progress: PlayerProgress): RewardState = when {
        progress.claimedRewards.contains(rewardId) -> RewardState.CLAIMED
        progress.routesFinished >= requiredRoutes -> RewardState.UNLOCKED
        else -> RewardState.LOCKED
    }
}
