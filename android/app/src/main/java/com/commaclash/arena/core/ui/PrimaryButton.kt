package com.commaclash.arena.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.ArenaPrimaryDeep
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.SurfaceMuted
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.theme.TextSecondary

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 60.dp,
    gradient: List<Color> = listOf(ArenaPrimary, ArenaPrimaryDeep),
    labelColor: Color = TextPrimary,
    shadowColor: Color = ArenaAccent,
    icon: Painter? = null,
    iconDescription: String? = null
) {
    val shape = RoundedCornerShape(6.dp)
    val offset = 5.dp
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(start = offset, top = offset)
                .background(if (enabled) shadowColor else SurfaceMuted, shape)
        )
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            border = BorderStroke(3.dp, InkDeep),
            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = labelColor,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = TextSecondary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = offset, bottom = offset)
                .height(height)
                .background(
                    if (enabled) {
                        Brush.linearGradient(gradient)
                    } else {
                        SolidColor(SurfaceMuted.copy(alpha = 0.45f))
                    },
                    shape
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Image(
                        painter = icon,
                        contentDescription = iconDescription,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
