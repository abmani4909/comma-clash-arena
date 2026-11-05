package com.commaclash.arena.presentation.app

import androidx.lifecycle.ViewModel
import com.commaclash.arena.core.di.ServiceLocator
import com.commaclash.arena.core.navigation.Screen
import com.commaclash.arena.domain.model.AppSettings
import com.commaclash.arena.domain.model.GameMode
import com.commaclash.arena.domain.model.RoundResult
import com.commaclash.arena.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(settingsRepository: SettingsRepository) : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    val settings: StateFlow<AppSettings> = settingsRepository.settings

    private val backStack = ArrayList<Screen>()
    private var roundToken = 0

    fun navigate(target: Screen) {
        backStack.add(_screen.value)
        _screen.value = target
    }

    fun startRound(mode: GameMode) {
        ServiceLocator.pendingMode = mode
        roundToken += 1
        backStack.clear()
        backStack.add(Screen.Menu)
        _screen.value = Screen.Game(mode, roundToken)
    }

    fun splashFinished() {
        if (_screen.value is Screen.Splash) {
            backStack.clear()
            _screen.value = Screen.Menu
        }
    }

    fun gameOver(result: RoundResult) {
        backStack.clear()
        backStack.add(Screen.Menu)
        _screen.value = Screen.GameOver(result, roundToken)
    }

    fun toMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        _screen.value = previous
        return true
    }
}
