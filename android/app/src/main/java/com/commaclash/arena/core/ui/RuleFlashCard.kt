package com.commaclash.arena.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.ArenaSuccess
import com.commaclash.arena.core.theme.SurfaceElevated
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.theme.TextSecondary

@Composable
fun RuleFlashCard(
    correct: Boolean,
    verdict: String,
    ruleName: String,
    ruleLine: String,
    rightLabel: String,
    rightExample: String,
    wrongLabel: String,
    wrongExample: String,
    modifier: Modifier = Modifier
) {
    InkBlock(
        modifier = modifier.fillMaxWidth(),
        containerColor = SurfaceElevated,
        shadowColor = if (correct) ArenaSuccess else ArenaPrimary,
        shadowOffset = 8.dp,
        contentPadding = PaddingValues(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatPill(
                    text = verdict,
                    color = if (correct) ArenaSuccess else ArenaPrimary,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = ruleName,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ruleLine,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = rightLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = ArenaSuccess
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = rightExample,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp
                    ),
                    color = ArenaSuccess
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = wrongLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = wrongExample,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp
                    ),
                    color = TextSecondary
                )
            }
        }
    }
}
