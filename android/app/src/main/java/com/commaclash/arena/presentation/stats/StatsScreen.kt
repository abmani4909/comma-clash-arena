package com.commaclash.arena.presentation.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commaclash.arena.R
import com.commaclash.arena.core.di.AppViewModelFactory
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaSuccess
import com.commaclash.arena.core.theme.TextSecondary
import com.commaclash.arena.core.ui.ArenaBackground
import com.commaclash.arena.core.ui.RuleAccuracyRow
import com.commaclash.arena.core.ui.ScreenHeader
import com.commaclash.arena.core.ui.StatCard
import com.commaclash.arena.core.ui.WeeklyTrendChart

@Composable
fun StatsRoute(onBack: () -> Unit) {
    val viewModel: StatsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    StatsScreen(state = state, onBack = onBack)
}

@Composable
fun StatsScreen(
    state: StatsUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArenaBackground(
        overlay = listOf(
            Color(0xFF1A1423).copy(alpha = 0.84f),
            Color(0xFF372549).copy(alpha = 0.64f),
            Color(0xFF1A1423).copy(alpha = 0.88f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.stats_title),
                backDescription = stringResource(R.string.cd_back),
                onBack = onBack,
                trailing = {
                    Image(
                        painter = painterResource(R.drawable.sprite_5),
                        contentDescription = stringResource(R.string.cd_stats),
                        modifier = Modifier.size(36.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                StatCard(
                    label = stringResource(R.string.stats_overall),
                    value = stringResource(R.string.percent_format, state.overall),
                    valueColor = ArenaSuccess,
                    modifier = Modifier
                        .weight(1f)
                        .height(92.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                StatCard(
                    label = stringResource(R.string.stats_rounds),
                    value = state.rounds.toString(),
                    valueColor = ArenaAccent,
                    modifier = Modifier
                        .weight(1f)
                        .height(92.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                StatCard(
                    label = stringResource(R.string.stats_best_streak),
                    value = state.bestStreak.toString(),
                    valueColor = ArenaSuccess,
                    modifier = Modifier
                        .weight(1f)
                        .height(92.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.stats_weekly),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            WeeklyTrendChart(days = state.week)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.stats_per_rule),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.ruleAccuracy) { rule ->
                    RuleAccuracyRow(
                        name = rule.name,
                        percent = rule.percent,
                        percentLabel = stringResource(R.string.percent_format, rule.percent),
                        description = stringResource(R.string.sd_rule_accuracy_format, rule.percent)
                    )
                }
            }
        }
    }
}
