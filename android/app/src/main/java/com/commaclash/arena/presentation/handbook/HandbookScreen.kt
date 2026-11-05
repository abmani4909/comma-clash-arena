package com.commaclash.arena.presentation.handbook

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commaclash.arena.R
import com.commaclash.arena.core.di.AppViewModelFactory
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaSuccess
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.SurfaceElevated
import com.commaclash.arena.core.theme.SurfacePanel
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.theme.TextSecondary
import com.commaclash.arena.core.ui.ArenaBackground
import com.commaclash.arena.core.ui.InkBlock
import com.commaclash.arena.core.ui.ScreenHeader

@Composable
fun HandbookRoute(onBack: () -> Unit) {
    val viewModel: HandbookViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    HandbookScreen(
        state = state,
        onBack = onBack,
        onQueryChange = viewModel::onQueryChange,
        onToggle = viewModel::toggle
    )
}

@Composable
fun HandbookScreen(
    state: HandbookUiState,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ArenaBackground(
        overlay = listOf(
            Color(0xFF1A1423).copy(alpha = 0.84f),
            Color(0xFF241B30).copy(alpha = 0.70f),
            Color(0xFF1A1423).copy(alpha = 0.88f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.handbook_title),
                backDescription = stringResource(R.string.cd_back),
                onBack = onBack,
                trailing = {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = stringResource(R.string.cd_handbook),
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                singleLine = true,
                shape = RoundedCornerShape(4.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.handbook_search),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfacePanel,
                    unfocusedContainerColor = SurfacePanel,
                    focusedBorderColor = ArenaAccent,
                    unfocusedBorderColor = InkDeep,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = ArenaAccent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (state.empty) {
                Text(
                    text = stringResource(R.string.handbook_empty),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    state.sections.forEach { section ->
                        item(key = section.title) {
                            Text(
                                text = section.title,
                                style = MaterialTheme.typography.labelSmall,
                                color = InkDeep,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ArenaAccent)
                                    .border(3.dp, InkDeep)
                                    .padding(horizontal = 8.dp, vertical = 7.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(
                            count = section.rules.size,
                            key = { index -> section.rules[index].id }
                        ) { index ->
                            val rule = section.rules[index]
                            InkBlock(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateContentSize(),
                                containerColor = SurfaceElevated,
                                shadowOffset = 5.dp,
                                contentPadding = PaddingValues(12.dp),
                                onClick = { onToggle(rule.id) }
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = rule.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = TextPrimary,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = rule.line,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                    if (state.expanded == rule.id) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = rule.rightExample,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 14.sp
                                            ),
                                            color = ArenaSuccess
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = rule.wrongExample,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 14.sp
                                            ),
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}
