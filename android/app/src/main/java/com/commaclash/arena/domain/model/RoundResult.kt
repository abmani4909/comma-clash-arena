package com.commaclash.arena.domain.model

enum class GameMode { ARENA, CLINIC }

data class RuleOutcome(val ruleId: String, val correct: Boolean)

data class RoundResult(
    val mode: GameMode = GameMode.ARENA,
    val correct: Int = 0,
    val total: Int = 0,
    val damage: Int = 0,
    val bestStreak: Int = 0,
    val brokenRules: List<String> = emptyList(),
    val rivalDown: Boolean = false
) {
    val accuracyPercent: Int get() = if (total == 0) 0 else correct * 100 / total
}
