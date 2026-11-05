package com.commaclash.arena.core.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.SurfaceElevated

@Composable
fun InkBlock(
    modifier: Modifier = Modifier,
    containerColor: Color = SurfaceElevated,
    containerBrush: Brush? = null,
    shadowColor: Color = InkDeep,
    shadowOffset: Dp = 6.dp,
    cornerRadius: Dp = 6.dp,
    borderColor: Color = InkDeep,
    borderWidth: Dp = 3.dp,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    onClick: (() -> Unit)? = null,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val shift by animateDpAsState(
        targetValue = if (pressed && onClick != null) shadowOffset else 0.dp,
        animationSpec = tween(90),
        label = "inkShift"
    )
    Box(
        modifier = modifier
            .offset(x = shift, y = shift)
            .drawBehind {
                val gap = (shadowOffset - shift).toPx()
                val radius = cornerRadius.toPx()
                drawRoundRect(
                    color = shadowColor,
                    topLeft = Offset(gap, gap),
                    size = size,
                    cornerRadius = CornerRadius(radius, radius)
                )
            }
            .background(containerBrush ?: SolidColor(containerColor), shape)
            .border(borderWidth, borderColor, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .padding(contentPadding),
        contentAlignment = contentAlignment,
        content = content
    )
}
