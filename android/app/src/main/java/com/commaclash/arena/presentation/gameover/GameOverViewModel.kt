package com.commaclash.arena.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commaclash.arena.domain.repository.ProgressRepository
import com.commaclash.arena.domain.repository.SentenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameOverUiState(
    val ruleNames: Map<String, String> = emptyMap(),
    val lifetimeAccuracy: Int = 0,
    val filedToClinic: Boolean = false
)

class GameOverViewModel(
    sentenceRepository: SentenceRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        GameOverUiState(ruleNames = sentenceRepository.rules().associate { it.id to it.name })
    )
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                _state.update { it.copy(lifetimeAccuracy = progress.accuracyPercent) }
            }
        }
    }

    fun sendToClinic(ruleIds: List<String>) {
        if (ruleIds.isEmpty() || _state.value.filedToClinic) return
        progressRepository.addToClinic(ruleIds)
        _state.update { it.copy(filedToClinic = true) }
    }
}
