package com.Qesnisu9.Madqis.core.navigation

import com.Qesnisu9.Madqis.domain.model.RunResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(
        val dailySeed: Int? = null,
        val token: Long = System.nanoTime()
    ) : Screen
    data class GameOver(val result: RunResult) : Screen
    data object DailyChallenge : Screen
    data object Progress : Screen
    data object Rewards : Screen
    data object Collection : Screen
    data object Settings : Screen
}
