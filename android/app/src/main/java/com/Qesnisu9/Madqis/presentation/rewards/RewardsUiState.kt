package com.Qesnisu9.Madqis.presentation.rewards

import com.Qesnisu9.Madqis.domain.model.Reward
import com.Qesnisu9.Madqis.domain.model.RewardState

data class RewardRow(
    val reward: Reward,
    val state: RewardState
)

data class RewardsUiState(
    val loading: Boolean = true,
    val rows: List<RewardRow> = emptyList(),
    val routesFinished: Int = 0,
    val sugarCoins: Int = 0
)
