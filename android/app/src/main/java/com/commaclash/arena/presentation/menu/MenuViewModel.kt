package com.commaclash.arena.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commaclash.arena.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                _state.update {
                    it.copy(
                        tier = progress.tier,
                        accuracyPercent = progress.accuracyPercent,
                        bestStreak = progress.bestStreak,
                        clinicCount = progress.clinic.size,
                        rounds = progress.rounds
                    )
                }
            }
        }
    }
}
