package com.commaclash.arena.core.di

import android.content.Context
import com.commaclash.arena.data.local.AppPreferences
import com.commaclash.arena.data.repository.ProgressRepositoryImpl
import com.commaclash.arena.data.repository.SentenceRepositoryImpl
import com.commaclash.arena.data.repository.SettingsRepositoryImpl
import com.commaclash.arena.domain.model.GameMode
import com.commaclash.arena.domain.repository.ProgressRepository
import com.commaclash.arena.domain.repository.SentenceRepository
import com.commaclash.arena.domain.repository.SettingsRepository
import com.commaclash.arena.domain.usecase.BuildClinicRoundUseCase
import com.commaclash.arena.domain.usecase.BuildRoundUseCase
import com.commaclash.arena.domain.usecase.EvaluateAnswerUseCase
import com.commaclash.arena.domain.usecase.SearchRulesUseCase
import com.commaclash.arena.domain.usecase.UpdateProgressUseCase

object ServiceLocator {

    private var preferences: AppPreferences? = null

    var pendingMode: GameMode = GameMode.ARENA

    fun init(context: Context) {
        if (preferences == null) {
            preferences = AppPreferences(context)
        }
    }

    private val requirePreferences: AppPreferences
        get() = requireNotNull(preferences)

    val sentenceRepository: SentenceRepository by lazy { SentenceRepositoryImpl() }

    val progressRepository: ProgressRepository by lazy { ProgressRepositoryImpl(requirePreferences) }

    val settingsRepository: SettingsRepository by lazy { SettingsRepositoryImpl(requirePreferences) }

    val buildRound: BuildRoundUseCase by lazy { BuildRoundUseCase(sentenceRepository) }

    val buildClinicRound: BuildClinicRoundUseCase by lazy { BuildClinicRoundUseCase(sentenceRepository) }

    val evaluateAnswer: EvaluateAnswerUseCase by lazy { EvaluateAnswerUseCase() }

    val updateProgress: UpdateProgressUseCase by lazy {
        UpdateProgressUseCase(progressRepository, sentenceRepository)
    }

    val searchRules: SearchRulesUseCase by lazy { SearchRulesUseCase(sentenceRepository) }
}
