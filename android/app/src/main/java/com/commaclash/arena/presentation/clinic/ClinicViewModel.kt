package com.commaclash.arena.presentation.clinic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commaclash.arena.domain.repository.ProgressRepository
import com.commaclash.arena.domain.repository.SentenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ClinicEntry(
    val ruleId: String,
    val name: String,
    val line: String,
    val missCount: Int
)

data class ClinicUiState(val entries: List<ClinicEntry> = emptyList())

class ClinicViewModel(
    private val sentenceRepository: SentenceRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ClinicUiState())
    val state: StateFlow<ClinicUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                val entries = progress.clinic.entries
                    .mapNotNull { entry ->
                        val rule = sentenceRepository.ruleById(entry.key)
                        if (rule == null) {
                            null
                        } else {
                            ClinicEntry(rule.id, rule.name, rule.line, entry.value)
                        }
                    }
                    .sortedByDescending { it.missCount }
                _state.value = ClinicUiState(entries)
            }
        }
    }

    fun dismiss(ruleId: String) {
        progressRepository.clearClinicRule(ruleId)
    }
}
