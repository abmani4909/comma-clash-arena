package com.commaclash.arena.presentation.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.commaclash.arena.R
import com.commaclash.arena.core.theme.ArenaAccent
import com.commaclash.arena.core.theme.ArenaPrimary
import com.commaclash.arena.core.theme.InkDeep
import com.commaclash.arena.core.theme.SurfaceElevated
import com.commaclash.arena.core.theme.TextPrimary
import com.commaclash.arena.core.theme.TextSecondary

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onQuit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = SurfaceElevated,
        shape = RoundedCornerShape(6.dp),
        tonalElevation = 0.dp,
        title = {
            Text(
                text = stringResource(R.string.game_paused),
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_dialog_body),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(
                    text = stringResource(R.string.resume),
                    style = MaterialTheme.typography.titleMedium,
                    color = ArenaAccent
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onQuit,
                border = BorderStroke(3.dp, ArenaPrimary),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.quit_round),
                    style = MaterialTheme.typography.titleMedium,
                    color = ArenaPrimary
                )
            }
        },
        iconContentColor = InkDeep
    )
}
