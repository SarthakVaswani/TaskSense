package com.example.taskappai.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskappai.domain.model.Priority
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBottomSheet1(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onTaskAdded: (String, String, Long, Priority, String) -> Unit,
    isLoading: Boolean = false,
    aiSuggestedPriority: Priority? = null,
    onDescriptionChange: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.Medium) }
    var category by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(0L) }


    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false,
                confirmValueChange = { true }
            ),
            contentWindowInsets = { WindowInsets(0.dp) },
            modifier = Modifier.fillMaxHeight(0.7f),
            dragHandle = { /* Your custom drag handle */ }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Add New Task",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        onDescriptionChange(it)
                    },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 2
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }

                aiSuggestedPriority?.let { suggested ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "AI Suggested:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        AssistChip(
                            onClick = { priority = suggested },
                            label = { Text(suggested.name) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }

                Text(
                    "Select Priority:",
                    style = MaterialTheme.typography.bodyMedium
                )

                PrioritySelector(
                    selectedPriority = priority,
                    onPrioritySelected = { priority = it }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            onTaskAdded(title, description, dueDate, priority, category)
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Add Task")
                    }
                }
            }
        }
    }
}