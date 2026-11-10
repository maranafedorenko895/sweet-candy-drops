package com.Qesnisu9.Madqis.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository
import com.Qesnisu9.Madqis.domain.repository.SettingsRepository
import com.Qesnisu9.Madqis.domain.usecase.EvaluateDailyRouteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val evaluateDailyRoute: EvaluateDailyRouteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val settings = settingsRepository.load()
            val daily = evaluateDailyRoute(progress)
            val ratio = if (daily.target > 0) {
                (progress.bestScore.toFloat() / daily.target.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
            _state.update {
                it.copy(
                    loading = false,
                    bestScore = progress.bestScore,
                    routesFinished = progress.routesFinished,
                    treats = progress.treatsCollected,
                    sugarCoins = progress.sugarCoins,
                    dailyTitle = daily.title,
                    dailyTarget = daily.target,
                    dailyClaimed = daily.claimed,
                    dailyProgress = if (daily.claimed) 1f else ratio,
                    reducedMotion = settings.reducedMotion
                )
            }
        }
    }
}
