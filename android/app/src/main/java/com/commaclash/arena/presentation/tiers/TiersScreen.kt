package com.commaclash.arena.presentation.tiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commaclash.arena.R
import com.commaclash.arena.core.di.AppViewModelFactory
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.TextSecondary
import com.commaclash.arena.core.ui.ArenaBackground
import com.commaclash.arena.core.ui.ScreenHeader
import com.commaclash.arena.core.ui.TierRow
import com.commaclash.arena.domain.model.TierState

@Composable
fun TiersRoute(onBack: () -> Unit) {
    val viewModel: TiersViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    TiersScreen(
        state = state,
        onBack = onBack,
        onSelect = { tierId ->
            if (viewModel.selectTier(tierId)) onBack()
        }
    )
}

@Composable
fun TiersScreen(
    state: TiersUiState,
    onBack: () -> Unit,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val badges = listOf(
        R.drawable.sprite_2,
        R.drawable.sprite_3,
        R.drawable.sprite_4,
        R.drawable.sprite_5,
        R.drawable.sprite_1
    )
    ArenaBackground(
        overlay = listOf(
            Color(0xFF1A1423).copy(alpha = 0.82f),
            Color(0xFF372549).copy(alpha = 0.66f),
            Color(0xFF1A1423).copy(alpha = 0.86f)
        ),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 96.dp)
                .height(18.dp)
                .graphicsLayer { rotationZ = -12f }
                .background(ArenaAccent.copy(alpha = 0.14f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.tiers_title),
                backDescription = stringResource(R.string.cd_back),
                onBack = onBack
            )
            if (state.lockedNotice) {
                Text(
                    text = stringResource(R.string.tier_locked_hint),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.tiers, key = { it.id }) { tier ->
                    TierRow(
                        title = tier.title,
                        subtitle = stringResource(
                            R.string.tier_rules_format,
                            tier.ruleCount,
                            tier.masteredCount,
                            tier.ruleCount
                        ),
                        state = tier.state,
                        chipText = when (tier.state) {
                            TierState.OPEN -> stringResource(R.string.tier_state_open)
                            TierState.ACTIVE -> stringResource(R.string.tier_state_now)
                            TierState.LOCKED -> stringResource(R.string.tier_state_locked)
                        },
                        chipDescription = when (tier.state) {
                            TierState.LOCKED -> stringResource(R.string.sd_locked)
                            else -> stringResource(R.string.sd_unlocked)
                        },
                        badge = painterResource(badges[(tier.id - 1) % badges.size]),
                        badgeDescription = stringResource(R.string.cd_tier_badge),
                        onClick = { onSelect(tier.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
