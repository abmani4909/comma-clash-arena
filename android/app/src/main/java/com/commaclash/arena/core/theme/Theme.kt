package com.commaclash.arena.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val LocalSentenceSize = compositionLocalOf<TextUnit> { 22.sp }

private val ArenaColorScheme = darkColorScheme(
    primary = ArenaPrimary,
    onPrimary = TextPrimary,
    primaryContainer = ArenaPrimaryDeep,
    onPrimaryContainer = TextPrimary,
    secondary = ArenaAccent,
    onSecondary = InkDeep,
    secondaryContainer = ArenaAccentDeep,
    onSecondaryContainer = InkDeep,
    tertiary = ArenaSuccess,
    onTertiary = InkDeep,
    background = BackgroundBase,
    onBackground = TextPrimary,
    surface = SurfacePanel,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    surfaceContainer = SurfaceElevated,
    surfaceContainerHigh = SurfaceElevated,
    outline = InkDeep,
    outlineVariant = SurfaceMuted,
    error = ArenaPrimary,
    onError = TextPrimary,
    scrim = InkDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ArenaColorScheme,
        typography = ArenaTypography,
        content = content
    )
}
