package com.Qesnisu9.Madqis.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qesnisu9.Madqis.data.sample.SampleData
import com.Qesnisu9.Madqis.domain.model.PlayerProgress
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository
import com.Qesnisu9.Madqis.domain.usecase.ClaimRewardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RewardsViewModel(
    private val progressRepository: ProgressRepository,
    private val claimReward: ClaimRewardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RewardsUiState())
    val state: StateFlow<RewardsUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            apply(progressRepository.load())
        }
    }

    fun claim(rewardId: Int) {
        viewModelScope.launch {
            apply(claimReward(rewardId))
        }
    }

    private fun apply(progress: PlayerProgress) {
        val rows = SampleData.rewardLadder.map { reward ->
            RewardRow(
                reward = reward,
                state = claimReward.stateOf(reward.id, reward.requiredRoutes, progress)
            )
        }
        _state.update {
            it.copy(
                loading = false,
                rows = rows,
                routesFinished = progress.routesFinished,
                sugarCoins = progress.sugarCoins
            )
        }
    }
}
