package com.commaclash.arena.presentation.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commaclash.arena.domain.model.AppSettings
import com.commaclash.arena.domain.model.SpellingVariant
import com.commaclash.arena.domain.model.TextSizeOption
import com.commaclash.arena.domain.repository.ProgressRepository
import com.commaclash.arena.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OptionsUiState(
    val settings: AppSettings = AppSettings(),
    val confirmReset: Boolean = false
)

class OptionsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OptionsUiState())
    val state: StateFlow<OptionsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.update { it.copy(settings = settings) }
            }
        }
    }

    fun setTextSize(option: TextSizeOption) = settingsRepository.setTextSize(option)

    fun setTimerEnabled(enabled: Boolean) = settingsRepository.setTimerEnabled(enabled)

    fun setHapticsEnabled(enabled: Boolean) = settingsRepository.setHapticsEnabled(enabled)

    fun setSpelling(variant: SpellingVariant) = settingsRepository.setSpelling(variant)

    fun askReset() {
        _state.update { it.copy(confirmReset = true) }
    }

    fun dismissReset() {
        _state.update { it.copy(confirmReset = false) }
    }

    fun confirmReset() {
        progressRepository.reset()
        _state.update { it.copy(confirmReset = false) }
    }
}
