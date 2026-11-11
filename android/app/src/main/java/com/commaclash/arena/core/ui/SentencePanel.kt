package com.commaclash.arena.core.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.ArenaSuccess
import com.commaclash.arena.core.theme.LocalSentenceSize
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.domain.model.SentenceToken
import com.commaclash.arena.domain.model.TokenKind

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentencePanel(
    tokens: List<SentenceToken>,
    selected: Int,
    swapped: Boolean,
    resolved: Boolean,
    correct: Boolean,
    answerIndex: Int,
    insertMark: String,
    stampTick: Int,
    selectedLabel: String,
    unselectedLabel: String,
    enabled: Boolean,
    onToken: (SentenceToken) -> Unit,
    modifier: Modifier = Modifier
) {
    val stamp = remember { Animatable(1f) }
    LaunchedEffect(stampTick) {
        if (stampTick > 0) {
            stamp.snapTo(1.6f)
            stamp.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessHigh)
            )
        }
    }
    val sentenceSize = LocalSentenceSize.current
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        tokens.forEach { token ->
            val isSelected = token.index == selected
            val isAnswer = token.index == answerIndex
            when (token.kind) {
                TokenKind.WORD -> Box(
                    modifier = Modifier.height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = token.text,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = sentenceSize),
                        color = TextPrimary
                    )
                }

                TokenKind.GAP -> {
                    val width by animateDpAsState(
                        targetValue = if (isSelected) 40.dp else 12.dp,
                        animationSpec = tween(120),
                        label = "gapWidth"
                    )
                    Box(
                        modifier = Modifier
                            .width(width)
                            .height(48.dp)
                            .semantics {
                                stateDescription = if (isSelected) selectedLabel else unselectedLabel
                            }
                            .clickable(enabled = enabled) { onToken(token) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (resolved && correct && isAnswer) {
                            Text(
                                text = insertMark,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = sentenceSize,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = ArenaSuccess,
                                modifier = Modifier.scale(stamp.value)
                            )
                        } else if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(28.dp)
                                    .background(ArenaAccent, RoundedCornerShape(2.dp))
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(16.dp)
                                    .background(Color.White.copy(alpha = 0.10f))
                            )
                        }
                    }
                }

                TokenKind.MARK -> Box(
                    modifier = Modifier
                        .height(48.dp)
                        .semantics {
                            stateDescription = if (isSelected) selectedLabel else unselectedLabel
                        }
                        .clickable(enabled = enabled) { onToken(token) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = token.text,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = sentenceSize,
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (isSelected) ArenaPrimary else ArenaAccent,
                        modifier = Modifier.scale(if (resolved && correct && isAnswer) stamp.value else 1f)
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .height(3.dp)
                                .background(ArenaPrimary)
                        )
                    }
                }

                TokenKind.SWAP -> {
                    val shown = if (isSelected && swapped) token.alternate else token.text
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .padding(horizontal = 2.dp, vertical = 4.dp)
                            .border(
                                2.dp,
                                if (isSelected) ArenaAccent else ArenaAccent.copy(alpha = 0.45f),
                                RoundedCornerShape(4.dp)
                            )
                            .semantics {
                                stateDescription = if (isSelected) selectedLabel else unselectedLabel
                            }
                            .clickable(enabled = enabled) { onToken(token) }
                            .padding(horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Crossfade(targetState = shown, animationSpec = tween(140), label = "swap") { value ->
                            Text(
                                text = value,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = sentenceSize,
                                    fontFamily = FontFamily.Serif
                                ),
                                color = if (resolved && correct && isAnswer) ArenaSuccess else TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
