package com.commaclash.arena.core.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaSuccess
import com.commaclash.arena.core.theme.SurfaceElevated
import com.commaclash.arena.core.theme.SurfaceMuted
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.theme.TextSecondary
import com.commaclash.arena.domain.model.TierState

@Composable
fun TierRow(
    title: String,
    subtitle: String,
    state: TierState,
    chipText: String,
    chipDescription: String,
    badge: Painter,
    badgeDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "tierChip")
    val pulse by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "tierPulse"
    )
    val chipColor = when (state) {
        TierState.OPEN -> ArenaSuccess
        TierState.ACTIVE -> ArenaAccent
        TierState.LOCKED -> SurfaceMuted
    }
    InkBlock(
        modifier = modifier
            .fillMaxWidth()
            .height(104.dp)
            .semantics { stateDescription = chipDescription },
        containerColor = SurfaceElevated,
        shadowOffset = 5.dp,
        contentPadding = PaddingValues(12.dp),
        onClick = onClick,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = badge,
                contentDescription = badgeDescription,
                modifier = Modifier
                    .size(56.dp)
                    .alpha(if (state == TierState.LOCKED) 0.5f else 1f),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (state == TierState.LOCKED) TextSecondary else TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            StatPill(
                text = chipText,
                color = chipColor,
                modifier = Modifier.alpha(if (state == TierState.ACTIVE) pulse else 1f)
            )
        }
    }
}
