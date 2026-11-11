package com.commaclash.arena.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.commaclash.arena.presentation.app.AppViewModel
import com.commaclash.arena.presentation.clinic.ClinicViewModel
import com.commaclash.arena.presentation.game.GameViewModel
import com.commaclash.arena.presentation.gameover.GameOverViewModel
import com.commaclash.arena.presentation.handbook.HandbookViewModel
import com.commaclash.arena.presentation.menu.MenuViewModel
import com.commaclash.arena.presentation.options.OptionsViewModel
import com.commaclash.arena.presentation.splash.SplashViewModel
import com.commaclash.arena.presentation.stats.StatsViewModel
import com.commaclash.arena.presentation.tiers.TiersViewModel

object AppViewModelFactory {

    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { AppViewModel(ServiceLocator.settingsRepository) }
        initializer { SplashViewModel() }
        initializer { MenuViewModel(ServiceLocator.progressRepository) }
        initializer {
            TiersViewModel(ServiceLocator.sentenceRepository, ServiceLocator.progressRepository)
        }
        initializer {
            GameViewModel(
                mode = ServiceLocator.pendingMode,
                buildRound = ServiceLocator.buildRound,
                buildClinicRound = ServiceLocator.buildClinicRound,
                evaluateAnswer = ServiceLocator.evaluateAnswer,
                updateProgress = ServiceLocator.updateProgress,
                sentenceRepository = ServiceLocator.sentenceRepository,
                progressRepository = ServiceLocator.progressRepository,
                settingsRepository = ServiceLocator.settingsRepository
            )
        }
        initializer {
            GameOverViewModel(ServiceLocator.sentenceRepository, ServiceLocator.progressRepository)
        }
        initializer {
            ClinicViewModel(ServiceLocator.sentenceRepository, ServiceLocator.progressRepository)
        }
        initializer {
            HandbookViewModel(ServiceLocator.sentenceRepository, ServiceLocator.searchRules)
        }
        initializer {
            StatsViewModel(ServiceLocator.sentenceRepository, ServiceLocator.progressRepository)
        }
        initializer {
            OptionsViewModel(ServiceLocator.settingsRepository, ServiceLocator.progressRepository)
        }
    }
}
