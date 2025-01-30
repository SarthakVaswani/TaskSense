package com.example.taskappai.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskappai.domain.model.TaskError
import com.example.taskappai.ui.theme.*

@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    error: TaskError,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Error Icon",
                    tint = IconPink
                )
                Text(
                    "Error",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = { 
            Text(
                error.message,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            ) 
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) { 
                Text(
                    "OK",
                    color = IconPink,
                    fontWeight = FontWeight.Medium
                ) 
            }
        },
        containerColor = White,
        iconContentColor = IconPink,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        modifier = modifier
    )
}