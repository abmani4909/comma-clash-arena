package com.commaclash.arena.domain.repository

import com.commaclash.arena.domain.model.PlayerProgress
import com.commaclash.arena.domain.model.RoundResult
import com.commaclash.arena.domain.model.RuleOutcome
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<PlayerProgress>
    fun applyRound(result: RoundResult, outcomes: List<RuleOutcome>, masteredTier: Boolean)
    fun selectTier(tier: Int)
    fun addToClinic(ruleIds: List<String>)
    fun clearClinicRule(ruleId: String)
    fun reset()
}
