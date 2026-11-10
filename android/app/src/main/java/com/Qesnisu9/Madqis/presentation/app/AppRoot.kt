package com.Qesnisu9.Madqis.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Qesnisu9.Madqis.core.di.ServiceLocator
import com.Qesnisu9.Madqis.core.navigation.Screen
import com.Qesnisu9.Madqis.core.theme.BackgroundTop
import com.Qesnisu9.Madqis.presentation.collection.CollectionPanel
import com.Qesnisu9.Madqis.presentation.daily.DailyChallengePanel
import com.Qesnisu9.Madqis.presentation.game.GameScreen
import com.Qesnisu9.Madqis.presentation.gameover.GameOverScreen
import com.Qesnisu9.Madqis.presentation.menu.MenuScreen
import com.Qesnisu9.Madqis.presentation.progress.ProgressPanel
import com.Qesnisu9.Madqis.presentation.rewards.RewardsPanel
import com.Qesnisu9.Madqis.presentation.settings.SettingsPanel
import com.Qesnisu9.Madqis.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundTop)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(animationSpec = tween(600)) togetherWith fadeOut(animationSpec = tween(400))
            },
            label = "screen"
        ) { target ->
            when (target) {
                is Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.resetTo(Screen.Menu) }
                )
                is Screen.Menu -> MenuScreen(
                    onPlay = { appViewModel.navigate(Screen.Game()) },
                    onDaily = { appViewModel.navigate(Screen.DailyChallenge) },
                    onProgress = { appViewModel.navigate(Screen.Progress) },
                    onRewards = { appViewModel.navigate(Screen.Rewards) },
                    onCollection = { appViewModel.navigate(Screen.Collection) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )
                is Screen.Game -> GameScreen(
                    dailySeed = target.dailySeed,
                    token = target.token,
                    onFinished = { result -> appViewModel.replaceTop(Screen.GameOver(result)) },
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )
                is Screen.GameOver -> GameOverScreen(
                    result = target.result,
                    onPlayAgain = { appViewModel.replaceTop(Screen.Game()) },
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )
                is Screen.DailyChallenge -> DailyChallengePanel(
                    onBack = { appViewModel.back() },
                    onStart = { seed -> appViewModel.replaceTop(Screen.Game(dailySeed = seed)) }
                )
                is Screen.Progress -> ProgressPanel(onBack = { appViewModel.back() })
                is Screen.Rewards -> RewardsPanel(onBack = { appViewModel.back() })
                is Screen.Collection -> CollectionPanel(onBack = { appViewModel.back() })
                is Screen.Settings -> SettingsPanel(onBack = { appViewModel.back() })
            }
        }
    }
}
