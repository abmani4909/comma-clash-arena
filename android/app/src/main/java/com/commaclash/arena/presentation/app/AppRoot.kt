package com.commaclash.arena.presentation.app

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commaclash.arena.core.config.GameConfig
import com.commaclash.arena.core.di.AppViewModelFactory
import com.commaclash.arena.core.navigation.Screen
import com.commaclash.arena.core.theme.BackgroundBase
import com.commaclash.arena.core.theme.LocalSentenceSize
import com.commaclash.arena.domain.model.GameMode
import com.commaclash.arena.presentation.clinic.ClinicRoute
import com.commaclash.arena.presentation.game.GameRoute
import com.commaclash.arena.presentation.gameover.GameOverRoute
import com.commaclash.arena.presentation.handbook.HandbookRoute
import com.commaclash.arena.presentation.menu.MenuRoute
import com.commaclash.arena.presentation.options.OptionsRoute
import com.commaclash.arena.presentation.splash.SplashRoute
import com.commaclash.arena.presentation.stats.StatsRoute
import com.commaclash.arena.presentation.tiers.TiersRoute

@Composable
fun AppRoot() {
    val viewModel: AppViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val screen by viewModel.screen.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler(enabled = true) {
        if (!viewModel.back()) {
            (context as? Activity)?.finish()
        }
    }

    CompositionLocalProvider(LocalSentenceSize provides settings.textSize.size.sp) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundBase)
        ) {
            AnimatedContent(
                targetState = screen,
                transitionSpec = {
                    val duration = GameConfig.SCREEN_TRANSITION_MS
                    (fadeIn(tween(duration)) + slideInHorizontally(tween(duration)) { full -> full / 6 })
                        .togetherWith(
                            fadeOut(tween(duration)) +
                                slideOutHorizontally(tween(duration)) { full -> -full / 6 }
                        )
                },
                label = "screen"
            ) { current ->
                when (current) {
                    Screen.Splash -> SplashRoute(onFinished = viewModel::splashFinished)

                    Screen.Menu -> MenuRoute(
                        onPlay = { viewModel.startRound(GameMode.ARENA) },
                        onTiers = { viewModel.navigate(Screen.Tiers) },
                        onClinic = { viewModel.navigate(Screen.Clinic) },
                        onHandbook = { viewModel.navigate(Screen.Handbook) },
                        onStats = { viewModel.navigate(Screen.Stats) },
                        onOptions = { viewModel.navigate(Screen.Options) }
                    )

                    Screen.Tiers -> TiersRoute(onBack = { viewModel.back() })

                    is Screen.Game -> GameRoute(
                        token = current.token,
                        onFinished = viewModel::gameOver
                    )

                    is Screen.GameOver -> GameOverRoute(
                        result = current.result,
                        token = current.token,
                        onPlayAgain = { viewModel.startRound(current.result.mode) },
                        onMenu = viewModel::toMenu
                    )

                    Screen.Clinic -> ClinicRoute(
                        onBack = { viewModel.back() },
                        onStartDrill = { viewModel.startRound(GameMode.CLINIC) }
                    )

                    Screen.Handbook -> HandbookRoute(onBack = { viewModel.back() })

                    Screen.Stats -> StatsRoute(onBack = { viewModel.back() })

                    Screen.Options -> OptionsRoute(onBack = { viewModel.back() })
                }
            }
        }
    }
}
