package com.commaclash.arena.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.TextSecondary
import com.commaclash.arena.domain.model.WeekDayStat

@Composable
fun WeeklyTrendChart(
    days: List<WeekDayStat>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            if (days.isEmpty()) return@Canvas
            val slot = size.width / days.size
            val barWidth = slot * 0.56f
            val maxHeight = size.height - 8f
            days.forEachIndexed { index, day ->
                val value = day.percent.coerceAtLeast(0)
                val barHeight = maxHeight * (value / 100f).coerceIn(0.04f, 1f)
                val left = index * slot + (slot - barWidth) / 2f
                val top = size.height - barHeight
                if (day.percent >= 0) {
                    drawRect(
                        color = ArenaPrimary,
                        topLeft = Offset(left + 5f, top + 5f),
                        size = Size(barWidth, barHeight)
                    )
                    drawRect(
                        color = ArenaAccent,
                        topLeft = Offset(left, top),
                        size = Size(barWidth, barHeight)
                    )
                }
                drawRect(
                    color = InkDeep,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight),
                    style = Stroke(width = 6f)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { day ->
                Text(
                    text = day.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
