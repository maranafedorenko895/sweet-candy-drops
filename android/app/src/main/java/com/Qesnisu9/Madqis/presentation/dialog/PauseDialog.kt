package com.Qesnisu9.Madqis.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Qesnisu9.Madqis.R
import com.Qesnisu9.Madqis.core.theme.BackgroundTop
import com.Qesnisu9.Madqis.core.theme.Primary
import com.Qesnisu9.Madqis.core.theme.TextPrimary
import com.Qesnisu9.Madqis.core.theme.TextSecondary

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(28.dp),
        containerColor = BackgroundTop,
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                color = TextPrimary,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_message),
                color = TextSecondary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(
                    text = stringResource(R.string.resume),
                    color = Primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onMenu) {
                Text(
                    text = stringResource(R.string.menu),
                    color = TextSecondary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}
