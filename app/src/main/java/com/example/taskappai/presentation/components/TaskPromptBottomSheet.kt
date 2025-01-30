package com.example.taskappai.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskPromptBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onPromptSubmit: (String) -> Unit,
    isLoading: Boolean = false
) {
    var prompt by remember { mutableStateOf("") }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Describe Your Task",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = {
                        Text(
                            if (isLoading) "Adding your task"
                            else
                                "Example: Schedule a team meeting for next Monday at 10 AM to discuss the project timeline"
                        )
                    },
                    enabled = !isLoading
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        enabled = !isLoading
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            onPromptSubmit(prompt)
                            prompt = ""
                        },
                        enabled = prompt.isNotBlank() && !isLoading,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = colorScheme.onPrimary
                            )
                        } else {
                            Text("Create Task")
                        }
                    }
                }
            }
        }
    }
}