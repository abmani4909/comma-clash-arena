package com.commaclash.arena.presentation.menu

data class MenuUiState(
    val tier: Int = 1,
    val accuracyPercent: Int = 0,
    val bestStreak: Int = 0,
    val clinicCount: Int = 0,
    val rounds: Int = 0
)
