package com.commaclash.arena.presentation.gameover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commaclash.arena.R
import com.commaclash.arena.core.di.AppViewModelFactory
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.ArenaSuccess
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.theme.TextSecondary
import com.commaclash.arena.core.ui.ArenaBackground
import com.commaclash.arena.core.ui.InkBlock
import com.commaclash.arena.core.ui.PrimaryButton
import com.commaclash.arena.core.ui.SecondaryButton
import com.commaclash.arena.core.ui.StatCard
import com.commaclash.arena.domain.model.RoundResult

@Composable
fun GameOverRoute(
    result: RoundResult,
    token: Int,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(
        key = "gameover-" + token,
        factory = AppViewModelFactory.Factory
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    GameOverScreen(
        result = result,
        state = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu,
        onSendToClinic = { viewModel.sendToClinic(result.brokenRules) }
    )
}

@Composable
fun GameOverScreen(
    result: RoundResult,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    onSendToClinic: () -> Unit,
    modifier: Modifier = Modifier
) {
    val verdictVisible = remember { MutableTransitionState(false) }
    verdictVisible.targetState = true
    val rulesBroken = result.brokenRules.size
    val visibleCards = listOf(
        result.accuracyPercent > 0,
        rulesBroken > 0,
        result.damage > 0
    ).count { it }

    ArenaBackground(
        overlay = listOf(
            Color(0xFF1A1423).copy(alpha = 0.86f),
            Color(0xFF372549).copy(alpha = 0.64f),
            Color(0xFF1A1423).copy(alpha = 0.88f)
        ),
        modifier = modifier
    ) {
        repeat(8) { index ->
            Box(
                modifier = Modifier
                    .offset(
                        x = (24 + (index % 4) * 86).dp,
                        y = (120 + (index / 4) * 320).dp
                    )
                    .size(44.dp)
                    .graphicsLayer { rotationZ = (index * 17).toFloat() }
                    .background(
                        if (index % 2 == 0) ArenaAccent.copy(alpha = 0.12f) else ArenaPrimary.copy(alpha = 0.12f),
                        RoundedCornerShape(4.dp)
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            InkBlock(
                modifier = Modifier.size(120.dp),
                containerColor = ArenaPrimary,
                shadowColor = ArenaAccent,
                shadowOffset = 6.dp,
                contentPadding = PaddingValues(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        if (result.rivalDown) R.drawable.sprite_4 else R.drawable.sprite_2
                    ),
                    contentDescription = stringResource(R.string.cd_rival),
                    modifier = Modifier.size(88.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visibleState = verdictVisible,
                enter = fadeIn(tween(320)) + scaleIn(initialScale = 0.8f, animationSpec = tween(320))
            ) {
                Text(
                    text = if (result.rivalDown) {
                        stringResource(R.string.gameover_win)
                    } else {
                        stringResource(R.string.gameover_loss)
                    },
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (visibleCards >= 2) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (result.accuracyPercent > 0) {
                        StatCard(
                            label = stringResource(R.string.gameover_accuracy),
                            value = stringResource(R.string.percent_format, result.accuracyPercent),
                            valueColor = ArenaSuccess,
                            modifier = Modifier
                                .weight(1f)
                                .height(92.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    if (rulesBroken > 0) {
                        StatCard(
                            label = stringResource(R.string.gameover_rules_broken),
                            value = rulesBroken.toString(),
                            valueColor = ArenaPrimary,
                            modifier = Modifier
                                .weight(1f)
                                .height(92.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    if (result.damage > 0) {
                        StatCard(
                            label = stringResource(R.string.gameover_damage),
                            value = result.damage.toString(),
                            valueColor = ArenaAccent,
                            modifier = Modifier
                                .weight(1f)
                                .height(92.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (result.brokenRules.isEmpty()) {
                Text(
                    text = stringResource(R.string.gameover_flawless),
                    style = MaterialTheme.typography.titleMedium,
                    color = ArenaSuccess
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    result.brokenRules.take(3).forEach { ruleId ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(ArenaPrimary, RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.ruleNames[ruleId] ?: ruleId,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                height = 60.dp
            )
            if (result.brokenRules.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryButton(
                    text = if (state.filedToClinic) {
                        stringResource(R.string.sent_to_clinic)
                    } else {
                        stringResource(R.string.send_to_clinic)
                    },
                    onClick = onSendToClinic,
                    borderColor = if (state.filedToClinic) ArenaSuccess else ArenaAccent,
                    labelColor = if (state.filedToClinic) ArenaSuccess else TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                borderColor = InkDeep,
                icon = painterResource(R.drawable.icon_home),
                iconDescription = stringResource(R.string.cd_home)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.gameover_lifetime, state.lifetimeAccuracy),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
