package com.Qesnisu9.Madqis.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            _state.update {
                it.copy(
                    loading = false,
                    bestScore = progress.bestScore,
                    totalScore = progress.totalScore,
                    routesFinished = progress.routesFinished,
                    treatsCollected = progress.treatsCollected,
                    bestCombo = progress.bestCombo,
                    bestSegment = progress.bestSegment,
                    sugarCoins = progress.sugarCoins
                )
            }
        }
    }
}
