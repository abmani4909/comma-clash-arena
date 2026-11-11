package com.commaclash.arena.domain.repository

import com.commaclash.arena.domain.model.AppSettings
import com.commaclash.arena.domain.model.SpellingVariant
import com.commaclash.arena.domain.model.TextSizeOption
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<AppSettings>
    fun setTextSize(option: TextSizeOption)
    fun setTimerEnabled(enabled: Boolean)
    fun setHapticsEnabled(enabled: Boolean)
    fun setSpelling(variant: SpellingVariant)
}
