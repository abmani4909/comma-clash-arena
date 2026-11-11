package com.commaclash.arena.domain.usecase

import com.commaclash.arena.core.config.GameConfig
import com.commaclash.arena.domain.model.RoundResult
import com.commaclash.arena.domain.model.RuleOutcome
import com.commaclash.arena.domain.repository.ProgressRepository
import com.commaclash.arena.domain.repository.SentenceRepository

class UpdateProgressUseCase(
    private val progressRepository: ProgressRepository,
    private val sentenceRepository: SentenceRepository
) {

    operator fun invoke(result: RoundResult, outcomes: List<RuleOutcome>) {
        val current = progressRepository.progress.value
        val tier = sentenceRepository.tiers().firstOrNull { it.id == current.tier }
        val correctByRule = HashMap<String, Int>()
        val totalByRule = HashMap<String, Int>()
        outcomes.forEach { outcome ->
            totalByRule[outcome.ruleId] = (totalByRule[outcome.ruleId] ?: 0) + 1
            if (outcome.correct) {
                correctByRule[outcome.ruleId] = (correctByRule[outcome.ruleId] ?: 0) + 1
            }
        }
        val mastered = tier != null && tier.ruleIds.all { ruleId ->
            val answered = (current.ruleTotal[ruleId] ?: 0) + (totalByRule[ruleId] ?: 0)
            val right = (current.ruleCorrect[ruleId] ?: 0) + (correctByRule[ruleId] ?: 0)
            answered > 0 && right * 100 / answered >= GameConfig.MASTERY_THRESHOLD
        }
        progressRepository.applyRound(result, outcomes, mastered)
    }
}
