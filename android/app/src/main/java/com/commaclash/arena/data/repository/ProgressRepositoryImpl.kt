package com.commaclash.arena.data.repository

import com.commaclash.arena.core.config.GameConfig
import com.commaclash.arena.data.local.AppPreferences
import com.commaclash.arena.domain.model.PlayerProgress
import com.commaclash.arena.domain.model.RoundResult
import com.commaclash.arena.domain.model.RuleOutcome
import com.commaclash.arena.domain.repository.ProgressRepository
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(private val preferences: AppPreferences) : ProgressRepository {

    private val state = MutableStateFlow(preferences.readProgress())

    override val progress: StateFlow<PlayerProgress> = state.asStateFlow()

    override fun applyRound(result: RoundResult, outcomes: List<RuleOutcome>, masteredTier: Boolean) {
        val current = state.value
        val ruleCorrect = HashMap(current.ruleCorrect)
        val ruleTotal = HashMap(current.ruleTotal)
        outcomes.forEach { outcome ->
            ruleTotal[outcome.ruleId] = (ruleTotal[outcome.ruleId] ?: 0) + 1
            if (outcome.correct) {
                ruleCorrect[outcome.ruleId] = (ruleCorrect[outcome.ruleId] ?: 0) + 1
            }
        }
        val clinic = HashMap(current.clinic)
        result.brokenRules.forEach { ruleId ->
            clinic[ruleId] = (clinic[ruleId] ?: 0) + 1
        }
        val unlocked = if (masteredTier) {
            minOf(GameConfig.TIER_COUNT, maxOf(current.unlockedTier, current.tier + 1))
        } else {
            current.unlockedTier
        }
        val weekly = ArrayList(current.weekly)
        val dayIndex = todayIndex()
        if (dayIndex in weekly.indices) {
            val previous = weekly[dayIndex]
            weekly[dayIndex] = if (previous < 0) {
                result.accuracyPercent
            } else {
                (previous + result.accuracyPercent) / 2
            }
        }
        val updated = current.copy(
            unlockedTier = unlocked,
            rounds = current.rounds + 1,
            bestStreak = maxOf(current.bestStreak, result.bestStreak),
            totalCorrect = current.totalCorrect + result.correct,
            totalAnswers = current.totalAnswers + result.total,
            ruleCorrect = ruleCorrect,
            ruleTotal = ruleTotal,
            clinic = clinic,
            weekly = weekly
        )
        persist(updated)
    }

    override fun selectTier(tier: Int) {
        val current = state.value
        if (tier > current.unlockedTier) return
        persist(current.copy(tier = tier))
    }

    override fun addToClinic(ruleIds: List<String>) {
        if (ruleIds.isEmpty()) return
        val current = state.value
        val clinic = HashMap(current.clinic)
        ruleIds.forEach { ruleId ->
            clinic[ruleId] = (clinic[ruleId] ?: 0) + 1
        }
        persist(current.copy(clinic = clinic))
    }

    override fun clearClinicRule(ruleId: String) {
        val current = state.value
        if (!current.clinic.containsKey(ruleId)) return
        val clinic = HashMap(current.clinic)
        clinic.remove(ruleId)
        persist(current.copy(clinic = clinic))
    }

    override fun reset() {
        preferences.clearProgress()
        state.value = PlayerProgress()
    }

    private fun persist(progress: PlayerProgress) {
        preferences.writeProgress(progress)
        state.value = progress
    }

    private fun todayIndex(): Int {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            else -> 6
        }
    }
}
