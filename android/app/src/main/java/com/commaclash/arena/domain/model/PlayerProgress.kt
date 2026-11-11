package com.commaclash.arena.domain.model

data class WeekDayStat(val label: String, val percent: Int)

data class PlayerProgress(
    val tier: Int = 1,
    val unlockedTier: Int = 1,
    val rounds: Int = 0,
    val bestStreak: Int = 0,
    val totalCorrect: Int = 0,
    val totalAnswers: Int = 0,
    val ruleCorrect: Map<String, Int> = emptyMap(),
    val ruleTotal: Map<String, Int> = emptyMap(),
    val clinic: Map<String, Int> = emptyMap(),
    val weekly: List<Int> = List(7) { -1 }
) {
    val accuracyPercent: Int get() = if (totalAnswers == 0) 0 else totalCorrect * 100 / totalAnswers

    fun rulePercent(ruleId: String): Int {
        val answered = ruleTotal[ruleId] ?: 0
        if (answered == 0) return 0
        return (ruleCorrect[ruleId] ?: 0) * 100 / answered
    }

    fun ruleAnswered(ruleId: String): Boolean = (ruleTotal[ruleId] ?: 0) > 0
}
