package com.commaclash.arena.presentation.options

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commaclash.arena.R
import com.commaclash.arena.core.di.AppViewModelFactory
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.SurfaceElevated
import com.commaclash.arena.core.theme.SurfaceMuted
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.theme.TextSecondary
import com.commaclash.arena.core.ui.ArenaBackground
import com.commaclash.arena.core.ui.InkBlock
import com.commaclash.arena.core.ui.ScreenHeader
import com.commaclash.arena.core.ui.SecondaryButton
import com.commaclash.arena.domain.model.SpellingVariant
import com.commaclash.arena.domain.model.TextSizeOption
import com.commaclash.arena.presentation.dialog.ConfirmResetDialog

@Composable
fun OptionsRoute(onBack: () -> Unit) {
    val viewModel: OptionsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    OptionsScreen(
        state = state,
        onBack = onBack,
        onTextSize = viewModel::setTextSize,
        onTimer = viewModel::setTimerEnabled,
        onHaptics = viewModel::setHapticsEnabled,
        onSpelling = viewModel::setSpelling,
        onAskReset = viewModel::askReset,
        onDismissReset = viewModel::dismissReset,
        onConfirmReset = viewModel::confirmReset
    )
}

@Composable
fun OptionsScreen(
    state: OptionsUiState,
    onBack: () -> Unit,
    onTextSize: (TextSizeOption) -> Unit,
    onTimer: (Boolean) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onSpelling: (SpellingVariant) -> Unit,
    onAskReset: () -> Unit,
    onDismissReset: () -> Unit,
    onConfirmReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    ArenaBackground(
        overlay = listOf(
            Color(0xFF1A1423).copy(alpha = 0.86f),
            Color(0xFF372549).copy(alpha = 0.62f),
            Color(0xFF1A1423).copy(alpha = 0.90f)
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
                title = stringResource(R.string.options_title),
                backDescription = stringResource(R.string.cd_back),
                onBack = onBack
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                SettingBlock(title = stringResource(R.string.options_text_size)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextSizeOption.entries.forEach { option ->
                            ChoiceChip(
                                label = when (option) {
                                    TextSizeOption.NORMAL -> stringResource(R.string.options_size_normal)
                                    TextSizeOption.LARGE -> stringResource(R.string.options_size_large)
                                    TextSizeOption.HUGE -> stringResource(R.string.options_size_huge)
                                },
                                selected = state.settings.textSize == option,
                                onClick = { onTextSize(option) },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.options_preview),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontSize = state.settings.textSize.size.sp
                        ),
                        color = TextPrimary
                    )
                }

                SettingBlock(title = stringResource(R.string.options_round_timer)) {
                    ToggleRow(
                        caption = stringResource(R.string.options_timer_caption),
                        checked = state.settings.timerEnabled,
                        onCheckedChange = onTimer
                    )
                }

                SettingBlock(title = stringResource(R.string.options_haptics)) {
                    ToggleRow(
                        caption = stringResource(R.string.options_haptics_caption),
                        checked = state.settings.hapticsEnabled,
                        onCheckedChange = onHaptics
                    )
                }

                SettingBlock(title = stringResource(R.string.options_spelling)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        SpellingVariant.entries.forEach { variant ->
                            ChoiceChip(
                                label = when (variant) {
                                    SpellingVariant.UK -> stringResource(R.string.options_spelling_uk)
                                    SpellingVariant.US -> stringResource(R.string.options_spelling_us)
                                },
                                selected = state.settings.spelling == variant,
                                onClick = { onSpelling(variant) },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(
                    text = stringResource(R.string.reset_progress),
                    onClick = onAskReset,
                    borderColor = ArenaPrimary,
                    labelColor = ArenaPrimary
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        if (state.confirmReset) {
            ConfirmResetDialog(onConfirm = onConfirmReset, onDismiss = onDismissReset)
        }
    }
}

@Composable
private fun SettingBlock(
    title: String,
    content: @Composable () -> Unit
) {
    InkBlock(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        containerColor = SurfaceElevated,
        shadowOffset = 5.dp,
        contentPadding = PaddingValues(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = ArenaAccent
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun ToggleRow(
    caption: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = caption,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = InkDeep,
                checkedTrackColor = ArenaAccent,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = SurfaceMuted
            )
        )
    }
}

@Composable
private fun ChoiceChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier = modifier
            .height(48.dp)
            .background(if (selected) ArenaAccent else SurfaceMuted, shape)
            .border(3.dp, InkDeep, shape)
            .semantics { stateDescription = label }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) InkDeep else TextPrimary
        )
    }
}
