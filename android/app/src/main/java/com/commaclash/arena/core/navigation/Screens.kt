package com.commaclash.arena.core.navigation

import com.commaclash.arena.domain.model.GameMode
import com.commaclash.arena.domain.model.RoundResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Tiers : Screen
    data class Game(val mode: GameMode, val token: Int) : Screen
    data class GameOver(val result: RoundResult, val token: Int) : Screen
    data object Clinic : Screen
    data object Handbook : Screen
    data object Stats : Screen
    data object Options : Screen
}
