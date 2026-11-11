package com.commaclash.arena.domain.usecase

import com.commaclash.arena.domain.model.SentenceItem
import com.commaclash.arena.domain.repository.SentenceRepository

class BuildRoundUseCase(private val sentenceRepository: SentenceRepository) {

    operator fun invoke(tier: Int, count: Int): List<SentenceItem> {
        val tierDefinition = sentenceRepository.tiers().firstOrNull { it.id == tier }
            ?: sentenceRepository.tiers().first()
        return pickBalanced(sentenceRepository, tierDefinition.ruleIds, count)
    }
}

internal fun pickBalanced(
    repository: SentenceRepository,
    ruleIds: List<String>,
    count: Int
): List<SentenceItem> {
    if (ruleIds.isEmpty()) return emptyList()
    val pools = ruleIds.associateWith { id ->
        repository.sentencesForRules(listOf(id)).shuffled().toMutableList()
    }
    val picked = ArrayList<SentenceItem>(count)
    var cursor = 0
    while (picked.size < count) {
        val ruleId = ruleIds[cursor % ruleIds.size]
        val pool = pools[ruleId]
        if (pool != null && pool.isNotEmpty()) {
            picked.add(pool.removeAt(0))
        } else if (pools.values.all { it.isEmpty() }) {
            val all = repository.sentencesForRules(ruleIds)
            if (all.isEmpty()) return picked
            picked.add(all[picked.size % all.size])
        }
        cursor++
        if (cursor > count * ruleIds.size + ruleIds.size) break
    }
    return picked.take(count)
}
