package com.commaclash.arena.domain.usecase

import com.commaclash.arena.domain.model.SentenceItem
import com.commaclash.arena.domain.repository.SentenceRepository

class BuildClinicRoundUseCase(private val sentenceRepository: SentenceRepository) {

    operator fun invoke(ruleIds: List<String>, count: Int): List<SentenceItem> {
        if (ruleIds.isEmpty()) return emptyList()
        return pickBalanced(sentenceRepository, ruleIds, count)
    }
}
