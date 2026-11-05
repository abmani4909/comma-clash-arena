package com.commaclash.arena.data.repository

import com.commaclash.arena.data.sample.SampleData
import com.commaclash.arena.domain.model.GrammarRule
import com.commaclash.arena.domain.model.RuleTier
import com.commaclash.arena.domain.model.SentenceItem
import com.commaclash.arena.domain.repository.SentenceRepository

class SentenceRepositoryImpl : SentenceRepository {

    private val ruleIndex: Map<String, GrammarRule> = SampleData.rules.associateBy { it.id }

    override fun tiers(): List<RuleTier> = SampleData.tiers

    override fun rules(): List<GrammarRule> = SampleData.rules

    override fun ruleById(ruleId: String): GrammarRule? = ruleIndex[ruleId]

    override fun sentencesForRules(ruleIds: List<String>): List<SentenceItem> {
        if (ruleIds.isEmpty()) return SampleData.sentences
        val wanted = ruleIds.toSet()
        return SampleData.sentences.filter { it.ruleId in wanted }
    }
}
