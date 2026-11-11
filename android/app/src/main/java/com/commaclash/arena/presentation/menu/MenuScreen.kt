package com.commaclash.arena.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commaclash.arena.R
import com.commaclash.arena.core.config.GameConfig
import com.commaclash.arena.core.di.AppViewModelFactory
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.ArenaPrimaryDeep
import com.commaclash.arena.core.theme.ArenaSuccess
import com.commaclash.arena.core.theme.SurfaceElevated
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.theme.TextSecondary
import com.commaclash.arena.core.ui.ArenaBackground
import com.commaclash.arena.core.ui.InkBlock
import com.commaclash.arena.core.ui.PrimaryButton
import com.commaclash.arena.core.ui.StatCard
import com.commaclash.arena.core.ui.StatPill

@Composable
fun MenuRoute(
    onPlay: () -> Unit,
    onTiers: () -> Unit,
    onClinic: () -> Unit,
    onHandbook: () -> Unit,
    onStats: () -> Unit,
    onOptions: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    MenuScreen(
        state = state,
        onPlay = onPlay,
        onTiers = onTiers,
        onClinic = onClinic,
        onHandbook = onHandbook,
        onStats = onStats,
        onOptions = onOptions
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onTiers: () -> Unit,
    onClinic: () -> Unit,
    onHandbook: () -> Unit,
    onStats: () -> Unit,
    onOptions: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArenaBackground(
        overlay = listOf(
            Color(0xFF1A1423).copy(alpha = 0.72f),
            Color(0xFF372549).copy(alpha = 0.58f),
            Color(0xFF1A1423).copy(alpha = 0.80f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.menu_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                StatPill(text = stringResource(R.string.menu_tier_format, state.tier))
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                InkBlock(
                    modifier = Modifier
                        .weight(2f)
                        .height(188.dp),
                    containerBrush = Brush.linearGradient(listOf(ArenaPrimary, ArenaPrimaryDeep)),
                    shadowColor = ArenaAccent,
                    shadowOffset = 6.dp,
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(R.drawable.sprite_1),
                            contentDescription = stringResource(R.string.cd_rival),
                            modifier = Modifier.size(64.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(
                                R.string.menu_play_caption,
                                GameConfig.SENTENCES_PER_ROUND,
                                state.tier
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary.copy(alpha = 0.82f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PrimaryButton(
                            text = stringResource(R.string.play),
                            onClick = onPlay,
                            height = 56.dp,
                            gradient = listOf(ArenaAccent, Color(0xFFE2C44A)),
                            labelColor = Color(0xFF0E0A14),
                            shadowColor = Color(0xFF0E0A14)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    StatCard(
                        label = stringResource(R.string.menu_accuracy),
                        value = stringResource(R.string.percent_format, state.accuracyPercent),
                        valueColor = ArenaSuccess,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(88.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    StatCard(
                        label = stringResource(R.string.menu_best_streak),
                        value = state.bestStreak.toString(),
                        valueColor = ArenaAccent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(88.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                MenuTile(
                    label = stringResource(R.string.menu_rule_tiers),
                    icon = painterResource(R.drawable.sprite_2),
                    iconDescription = stringResource(R.string.cd_tier_badge),
                    badge = "",
                    onClick = onTiers,
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                MenuTile(
                    label = stringResource(R.string.menu_rule_clinic),
                    icon = painterResource(R.drawable.sprite_3),
                    iconDescription = stringResource(R.string.cd_clinic),
                    badge = if (state.clinicCount > 0) state.clinicCount.toString() else "",
                    onClick = onClinic,
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                MenuTile(
                    label = stringResource(R.string.menu_style_handbook),
                    icon = painterResource(R.drawable.sprite_4),
                    iconDescription = stringResource(R.string.cd_handbook),
                    badge = "",
                    onClick = onHandbook,
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                MenuTile(
                    label = stringResource(R.string.menu_writer_stats),
                    icon = painterResource(R.drawable.sprite_5),
                    iconDescription = stringResource(R.string.cd_stats),
                    badge = "",
                    onClick = onStats,
                    modifier = Modifier
                        .weight(1f)
                        .height(92.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                MenuTile(
                    label = stringResource(R.string.menu_arena_options),
                    icon = painterResource(R.drawable.icon_pause),
                    iconDescription = stringResource(R.string.cd_options),
                    badge = "",
                    onClick = onOptions,
                    modifier = Modifier
                        .weight(1f)
                        .height(92.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.menu_footer),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun MenuTile(
    label: String,
    icon: Painter,
    iconDescription: String,
    badge: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InkBlock(
        modifier = modifier,
        containerColor = SurfaceElevated,
        shadowOffset = 5.dp,
        contentPadding = PaddingValues(8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                Image(
                    painter = icon,
                    contentDescription = iconDescription,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )
                if (badge.isNotEmpty()) {
                    StatPill(text = badge, color = ArenaPrimary, labelColor = TextPrimary)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}
