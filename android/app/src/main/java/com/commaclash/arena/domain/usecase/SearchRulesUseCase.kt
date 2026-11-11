package com.commaclash.arena.domain.usecase

import com.commaclash.arena.domain.model.GrammarRule
import com.commaclash.arena.domain.repository.SentenceRepository

class SearchRulesUseCase(private val sentenceRepository: SentenceRepository) {

    operator fun invoke(query: String): List<GrammarRule> {
        val rules = sentenceRepository.rules()
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return rules
        return rules.filter { rule ->
            rule.name.contains(trimmed, ignoreCase = true) ||
                rule.line.contains(trimmed, ignoreCase = true) ||
                rule.rightExample.contains(trimmed, ignoreCase = true)
        }
    }
}
