package com.Qesnisu9.Madqis.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.Qesnisu9.Madqis.domain.repository.ProgressRepository
import com.Qesnisu9.Madqis.domain.repository.SettingsRepository
import com.Qesnisu9.Madqis.domain.usecase.ClaimRewardUseCase
import com.Qesnisu9.Madqis.domain.usecase.EvaluateDailyRouteUseCase
import com.Qesnisu9.Madqis.domain.usecase.GenerateRouteUseCase
import com.Qesnisu9.Madqis.domain.usecase.ResolveStepUseCase
import com.Qesnisu9.Madqis.domain.usecase.SaveRunUseCase
import com.Qesnisu9.Madqis.presentation.app.AppViewModel
import com.Qesnisu9.Madqis.presentation.collection.CollectionViewModel
import com.Qesnisu9.Madqis.presentation.daily.DailyChallengeViewModel
import com.Qesnisu9.Madqis.presentation.game.GameViewModel
import com.Qesnisu9.Madqis.presentation.gameover.GameOverViewModel
import com.Qesnisu9.Madqis.presentation.menu.MenuViewModel
import com.Qesnisu9.Madqis.presentation.progress.ProgressViewModel
import com.Qesnisu9.Madqis.presentation.rewards.RewardsViewModel
import com.Qesnisu9.Madqis.presentation.settings.SettingsViewModel
import com.Qesnisu9.Madqis.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {

    private val evaluateDailyRoute = EvaluateDailyRouteUseCase()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val instance: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                progressRepository,
                settingsRepository,
                evaluateDailyRoute
            )
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                progressRepository,
                settingsRepository,
                evaluateDailyRoute,
                GenerateRouteUseCase(),
                ResolveStepUseCase(),
                SaveRunUseCase(progressRepository)
            )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                progressRepository
            )
            modelClass.isAssignableFrom(DailyChallengeViewModel::class.java) -> DailyChallengeViewModel(
                progressRepository,
                evaluateDailyRoute
            )
            modelClass.isAssignableFrom(ProgressViewModel::class.java) -> ProgressViewModel(
                progressRepository
            )
            modelClass.isAssignableFrom(RewardsViewModel::class.java) -> RewardsViewModel(
                progressRepository,
                ClaimRewardUseCase(progressRepository)
            )
            modelClass.isAssignableFrom(CollectionViewModel::class.java) -> CollectionViewModel(
                progressRepository
            )
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(
                progressRepository,
                settingsRepository
            )
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return instance as T
    }
}
