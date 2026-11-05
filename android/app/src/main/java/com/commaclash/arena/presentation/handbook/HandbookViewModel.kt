package com.commaclash.arena.presentation.handbook

import androidx.lifecycle.ViewModel
import com.commaclash.arena.domain.model.GrammarRule
import com.commaclash.arena.domain.repository.SentenceRepository
import com.commaclash.arena.domain.usecase.SearchRulesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HandbookSection(val title: String, val rules: List<GrammarRule>)

data class HandbookUiState(
    val query: String = "",
    val sections: List<HandbookSection> = emptyList(),
    val expanded: String = ""
) {
    val empty: Boolean get() = sections.isEmpty()
}

class HandbookViewModel(
    private val sentenceRepository: SentenceRepository,
    private val searchRules: SearchRulesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HandbookUiState(sections = group(sentenceRepository.rules())))
    val state: StateFlow<HandbookUiState> = _state.asStateFlow()

    fun onQueryChange(value: String) {
        _state.update { it.copy(query = value, sections = group(searchRules(value))) }
    }

    fun toggle(ruleId: String) {
        _state.update { it.copy(expanded = if (it.expanded == ruleId) "" else ruleId) }
    }

    private fun group(rules: List<GrammarRule>): List<HandbookSection> =
        sentenceRepository.tiers().mapNotNull { tier ->
            val inTier = rules.filter { it.tierId == tier.id }
            if (inTier.isEmpty()) null else HandbookSection(tier.title, inTier)
        }
}
