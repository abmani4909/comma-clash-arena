package com.commaclash.arena.domain.model

data class GrammarRule(
    val id: String,
    val tierId: Int,
    val name: String,
    val line: String,
    val rightExample: String,
    val wrongExample: String
)

data class RuleTier(
    val id: Int,
    val title: String,
    val ruleIds: List<String>
)

enum class TierState { OPEN, ACTIVE, LOCKED }
