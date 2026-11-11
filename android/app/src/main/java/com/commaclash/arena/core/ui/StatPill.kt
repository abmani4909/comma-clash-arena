package com.commaclash.arena.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.InkDeep

@Composable
fun StatPill(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = ArenaAccent,
    labelColor: Color = InkDeep
) {
    val shape = RoundedCornerShape(4.dp)
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = labelColor,
        modifier = modifier
            .background(color, shape)
            .border(3.dp, InkDeep, shape)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}
