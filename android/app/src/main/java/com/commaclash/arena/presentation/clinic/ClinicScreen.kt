package com.commaclash.arena.presentation.clinic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commaclash.arena.R
import com.commaclash.arena.core.di.AppViewModelFactory
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaAccentDeep
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.SurfaceElevated
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.theme.TextSecondary
import com.commaclash.arena.core.ui.ArenaBackground
import com.commaclash.arena.core.ui.InkBlock
import com.commaclash.arena.core.ui.PrimaryButton
import com.commaclash.arena.core.ui.ScreenHeader
import com.commaclash.arena.core.ui.StatPill

@Composable
fun ClinicRoute(onBack: () -> Unit, onStartDrill: () -> Unit) {
    val viewModel: ClinicViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    ClinicScreen(
        state = state,
        onBack = onBack,
        onStartDrill = onStartDrill,
        onDismiss = viewModel::dismiss
    )
}

@Composable
fun ClinicScreen(
    state: ClinicUiState,
    onBack: () -> Unit,
    onStartDrill: () -> Unit,
    onDismiss: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ArenaBackground(
        overlay = listOf(
            Color(0xFF1A1423).copy(alpha = 0.84f),
            Color(0xFF372549).copy(alpha = 0.62f),
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
                title = stringResource(R.string.clinic_title),
                backDescription = stringResource(R.string.cd_back),
                onBack = onBack,
                trailing = {
                    Image(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = stringResource(R.string.cd_clinic),
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
            Text(
                text = stringResource(R.string.clinic_subtitle),
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (state.entries.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_3),
                        contentDescription = stringResource(R.string.cd_clinic),
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.clinic_empty_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.clinic_empty_body),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.entries, key = { it.ruleId }) { entry ->
                        InkBlock(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(96.dp),
                            containerColor = SurfaceElevated,
                            shadowOffset = 5.dp,
                            contentPadding = PaddingValues(12.dp),
                            onClick = { onDismiss(entry.ruleId) },
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = entry.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = entry.line,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                StatPill(
                                    text = stringResource(R.string.clinic_missed_format, entry.missCount),
                                    color = ArenaPrimary,
                                    labelColor = TextPrimary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                PrimaryButton(
                    text = stringResource(R.string.start_clinic_drill),
                    onClick = onStartDrill,
                    height = 60.dp,
                    gradient = listOf(ArenaAccent, ArenaAccentDeep),
                    labelColor = InkDeep,
                    shadowColor = ArenaPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}
