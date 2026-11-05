package com.commaclash.arena.presentation.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commaclash.arena.R
import com.commaclash.arena.core.config.GameConfig
import com.commaclash.arena.core.di.AppViewModelFactory
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.ArenaSuccess
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.SurfaceMuted
import com.commaclash.arena.core.theme.SurfacePanel
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.ui.ArenaBackground
import com.commaclash.arena.core.ui.InkBlock
import com.commaclash.arena.core.ui.PrimaryButton
import com.commaclash.arena.core.ui.RivalHealthBar
import com.commaclash.arena.core.ui.RuleFlashCard
import com.commaclash.arena.core.ui.SentencePanel
import com.commaclash.arena.domain.model.RoundResult
import com.commaclash.arena.domain.model.SentenceToken
import com.commaclash.arena.presentation.dialog.PauseDialog
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameRoute(
    token: Int,
    onFinished: (RoundResult) -> Unit
) {
    val viewModel: GameViewModel = viewModel(
        key = "game-" + token,
        factory = AppViewModelFactory.Factory
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is GameEvent.Finished) onFinished(event.result)
        }
    }
    LaunchedEffect(state.hitTick) {
        if (state.hitTick > 0 && viewModel.hapticsEnabled) {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    GameScreen(
        state = state,
        onToken = viewModel::selectToken,
        onCheck = viewModel::check,
        onNext = viewModel::next,
        onPause = viewModel::openPause,
        onResume = viewModel::resume,
        onQuit = viewModel::quitRound
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onToken: (SentenceToken) -> Unit,
    onCheck: () -> Unit,
    onNext: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onQuit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val wiggle = remember { Animatable(0f) }
    LaunchedEffect(state.wiggleTick) {
        if (state.wiggleTick > 0) {
            wiggle.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = GameConfig.WIGGLE_MS
                    0f at 0
                    -10f at 40
                    10f at 90
                    -6f at 130
                    0f at GameConfig.WIGGLE_MS
                }
            )
        }
    }
    val recoil by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.RESOLVED && state.lastCorrect) 10f else 0f,
        animationSpec = tween(GameConfig.STAMP_MS),
        label = "recoil"
    )
    val burst by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.FINISHING) 1f else 0f,
        animationSpec = tween(GameConfig.BURST_MS.toInt(), easing = FastOutSlowInEasing),
        label = "burst"
    )
    val nerveDescription = stringResource(
        R.string.sd_nerve_format,
        (GameConfig.MISS_LIMIT - state.misses).coerceAtLeast(0),
        GameConfig.MISS_LIMIT
    )

    ArenaBackground(
        overlay = listOf(
            Color(0xFF0E0A14).copy(alpha = 0.80f),
            Color(0xFF1A1423).copy(alpha = 0.74f),
            Color(0xFF0E0A14).copy(alpha = 0.86f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(InkDeep.copy(alpha = 0.55f))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = onPause),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_pause),
                        contentDescription = stringResource(R.string.cd_pause),
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(
                            R.string.game_round_format,
                            state.index + 1,
                            state.total
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(horizontalArrangement = Arrangement.Center) {
                        state.pips.forEach { pip ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                                    .width(14.dp)
                                    .height(6.dp)
                                    .background(
                                        when (pip) {
                                            PipState.SOLVED -> ArenaSuccess
                                            PipState.MISSED -> ArenaPrimary
                                            PipState.PENDING -> SurfaceMuted
                                        },
                                        RoundedCornerShape(1.dp)
                                    )
                            )
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.game_streak_format, state.streak),
                        style = MaterialTheme.typography.titleMedium,
                        color = ArenaAccent,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = Modifier.semantics {
                            liveRegion = LiveRegionMode.Polite
                            contentDescription = nerveDescription
                        }
                    ) {
                        repeat(GameConfig.MISS_LIMIT) { slot ->
                            Box(
                                modifier = Modifier
                                    .padding(start = 3.dp)
                                    .size(10.dp)
                                    .background(
                                        if (slot < GameConfig.MISS_LIMIT - state.misses) {
                                            ArenaPrimary
                                        } else {
                                            SurfaceMuted
                                        },
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = stringResource(R.string.cd_rival),
                    modifier = Modifier
                        .size(52.dp)
                        .graphicsLayer {
                            translationX = recoil * 2f
                            rotationZ = -recoil
                        },
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(12.dp))
                RivalHealthBar(
                    fraction = state.rivalFraction,
                    description = stringResource(
                        R.string.sd_rival_hp_format,
                        (state.rivalFraction * 100).toInt()
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                InkBlock(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .graphicsLayer { translationX = wiggle.value },
                    containerColor = SurfacePanel,
                    shadowColor = if (state.lastCorrect && state.phase == GamePhase.RESOLVED) {
                        ArenaSuccess
                    } else {
                        Color(0xFF372549)
                    },
                    shadowOffset = 8.dp,
                    borderColor = if (state.phase == GamePhase.RESOLVED && !state.lastCorrect) {
                        ArenaPrimary
                    } else {
                        InkDeep
                    },
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (state.timerEnabled) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(state.timerFraction.coerceIn(0f, 1f))
                                    .height(6.dp)
                                    .background(ArenaAccent)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        val item = state.item
                        if (item != null) {
                            SentencePanel(
                                tokens = item.tokens,
                                selected = state.selected,
                                swapped = state.swapped,
                                resolved = state.phase != GamePhase.ANSWERING,
                                correct = state.lastCorrect,
                                answerIndex = item.answerIndex,
                                insertMark = item.insertMark,
                                stampTick = state.stampTick,
                                selectedLabel = stringResource(R.string.sd_selected),
                                unselectedLabel = stringResource(R.string.sd_not_selected),
                                enabled = state.phase == GamePhase.ANSWERING && !state.paused,
                                onToken = onToken
                            )
                        }
                    }
                }

                if (burst > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        repeat(12) { index ->
                            val angle = Math.toRadians((index * 30).toDouble())
                            Box(
                                modifier = Modifier
                                    .offset(
                                        x = (cos(angle) * 120f * burst).dp,
                                        y = (sin(angle) * 120f * burst).dp
                                    )
                                    .size(14.dp)
                                    .graphicsLayer {
                                        scaleX = burst
                                        scaleY = burst
                                        rotationZ = burst * 180f
                                    }
                                    .background(
                                        if (index % 2 == 0) ArenaAccent else ArenaPrimary,
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                        Text(
                            text = stringResource(R.string.game_ko),
                            style = MaterialTheme.typography.displaySmall,
                            color = ArenaAccent,
                            modifier = Modifier.graphicsLayer {
                                scaleX = 2.2f - burst * 1.2f
                                scaleY = 2.2f - burst * 1.2f
                                rotationZ = -8f
                            }
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.phase == GamePhase.RESOLVED && state.rule != null,
                enter = slideInVertically(tween(200)) { it } + fadeIn(tween(200)),
                exit = slideOutVertically(tween(200)) { it } + fadeOut(tween(200))
            ) {
                val rule = state.rule
                if (rule != null) {
                    RuleFlashCard(
                        correct = state.lastCorrect,
                        verdict = if (state.lastCorrect) {
                            stringResource(R.string.game_verdict_correct)
                        } else {
                            stringResource(R.string.game_verdict_missed)
                        },
                        ruleName = rule.name,
                        ruleLine = rule.line,
                        rightLabel = stringResource(R.string.game_right_label),
                        rightExample = rule.rightExample,
                        wrongLabel = stringResource(R.string.game_wrong_label),
                        wrongExample = rule.wrongExample,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(InkDeep.copy(alpha = 0.6f))
                    .border(3.dp, InkDeep)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                when (state.phase) {
                    GamePhase.ANSWERING -> PrimaryButton(
                        text = stringResource(R.string.check),
                        onClick = onCheck,
                        height = 60.dp
                    )

                    GamePhase.RESOLVED -> PrimaryButton(
                        text = if (state.lastSentence || state.rivalHp <= 0 || state.knockedOut) {
                            stringResource(R.string.see_scorecard)
                        } else {
                            stringResource(R.string.next)
                        },
                        onClick = onNext,
                        height = 60.dp,
                        gradient = listOf(ArenaAccent, Color(0xFFE2C44A)),
                        labelColor = InkDeep,
                        shadowColor = ArenaPrimary
                    )

                    GamePhase.FINISHING -> PrimaryButton(
                        text = stringResource(R.string.see_scorecard),
                        onClick = onNext,
                        enabled = false,
                        height = 60.dp
                    )
                }
            }
        }

        if (state.paused) {
            PauseDialog(onResume = onResume, onQuit = onQuit)
        }
    }
}
