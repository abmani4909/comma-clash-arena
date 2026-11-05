package com.commaclash.arena.domain.repository

import com.commaclash.arena.domain.model.GrammarRule
import com.commaclash.arena.domain.model.RuleTier
import com.commaclash.arena.domain.model.SentenceItem

interface SentenceRepository {
    fun tiers(): List<RuleTier>
    fun rules(): List<GrammarRule>
    fun ruleById(ruleId: String): GrammarRule?
    fun sentencesForRules(ruleIds: List<String>): List<SentenceItem>
}
