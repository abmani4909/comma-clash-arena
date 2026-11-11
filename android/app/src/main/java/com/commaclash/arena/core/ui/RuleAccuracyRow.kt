package com.commaclash.arena.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.ArenaSuccess
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.SurfaceElevated
import com.commaclash.arena.core.theme.TextPrimary

@Composable
fun RuleAccuracyRow(
    name: String,
    percent: Int,
    percentLabel: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val fillColor = when {
        percent >= 80 -> ArenaSuccess
        percent >= 50 -> ArenaAccent
        else -> ArenaPrimary
    }
    val shape = RoundedCornerShape(2.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .semantics { stateDescription = description },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary,
            modifier = Modifier.width(132.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(12.dp)
                .background(SurfaceElevated, shape)
                .border(2.dp, InkDeep, shape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth((percent / 100f).coerceIn(0f, 1f))
                    .background(fillColor, shape)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = percentLabel,
            style = MaterialTheme.typography.labelSmall,
            color = fillColor,
            modifier = Modifier.width(44.dp)
        )
    }
}
