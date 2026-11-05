package com.commaclash.arena.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.commaclash.arena.core.config.GameConfig
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.SurfaceElevated

@Composable
fun RivalHealthBar(
    fraction: Float,
    description: String,
    modifier: Modifier = Modifier
) {
    val animated by animateFloatAsState(
        targetValue = fraction.coerceIn(0f, 1f),
        animationSpec = tween(GameConfig.HP_DRAIN_MS, easing = FastOutSlowInEasing),
        label = "rivalHp"
    )
    val shape = RoundedCornerShape(2.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(SurfaceElevated, shape)
            .border(3.dp, InkDeep, shape)
            .semantics { stateDescription = description }
    ) {
        Box(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxHeight()
                .fillMaxWidth(animated)
                .background(Brush.horizontalGradient(listOf(ArenaPrimary, ArenaAccent)), shape)
        )
    }
}
