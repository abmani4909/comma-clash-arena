package com.commaclash.arena.data.repository

import com.commaclash.arena.data.local.AppPreferences
import com.commaclash.arena.domain.model.AppSettings
import com.commaclash.arena.domain.model.SpellingVariant
import com.commaclash.arena.domain.model.TextSizeOption
import com.commaclash.arena.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val preferences: AppPreferences) : SettingsRepository {

    private val state = MutableStateFlow(preferences.readSettings())

    override val settings: StateFlow<AppSettings> = state.asStateFlow()

    override fun setTextSize(option: TextSizeOption) = persist(state.value.copy(textSize = option))

    override fun setTimerEnabled(enabled: Boolean) = persist(state.value.copy(timerEnabled = enabled))

    override fun setHapticsEnabled(enabled: Boolean) = persist(state.value.copy(hapticsEnabled = enabled))

    override fun setSpelling(variant: SpellingVariant) = persist(state.value.copy(spelling = variant))

    private fun persist(settings: AppSettings) {
        preferences.writeSettings(settings)
        state.value = settings
    }
}
