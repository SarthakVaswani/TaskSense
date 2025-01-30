package com.example.taskappai.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.domain.model.Task
import com.example.taskappai.util.getPriorityColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskClick: () -> Unit,
    onTaskCheckedChanges: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onPriorityUpdate: (Priority) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { swipeToDismissBoxValue ->
            if (swipeToDismissBoxValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val alignment = Alignment.CenterEnd
            val icon = Icons.Default.Delete
            val scale by animateFloatAsState(
                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) 1.3f else 0.5f,
                label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Delete",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        content = {
            TaskItemContent(
                modifier = modifier,
                task = task,
                onTaskClick = onTaskClick,
                onTaskCheckedChanges = onTaskCheckedChanges,
                onPriorityUpdate = onPriorityUpdate
            )
        }
    )


}

@Composable
fun TaskItemContent(
    modifier: Modifier = Modifier,
    task: Task,
    onTaskClick: () -> Unit,
    onTaskCheckedChanges: (Boolean) -> Unit,
    onPriorityUpdate: (Priority) -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onTaskClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val scale by animateFloatAsState(
                if (task.isCompleted) 1.25f else 0.9f,
            )
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onTaskCheckedChanges,
                modifier = Modifier.scale(scale)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                if (task.description.isNotEmpty()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (task.dueDate != 0L) {
                    Text(
                        text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(
                            Date(
                                task.dueDate
                            )
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    PriorityChip(priority = task.priority)
                    task.aiSuggestedPriority?.let { suggestedPriority ->
                        if (task.priority != suggestedPriority)

                            PriorityChip(
                                priority = suggestedPriority,
                                isAiSuggestion = true,
                                modifier = Modifier,
                                onClick = { onPriorityUpdate(suggestedPriority) }

                            )
                    }
                }
            }
        }
    }

}

@Composable
fun PriorityChip(
    modifier: Modifier = Modifier,
    priority: Priority,
    isAiSuggestion: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val isEnabled = onClick != null
    val (containerColor, labelColor, _) = getPriorityColors(priority)

    AssistChip(
        onClick = {
            onClick?.invoke()
        },
        enabled = isEnabled,
        label = {
            Text(
                if (isAiSuggestion) "Suggestion : ${priority.name}" else priority.name,
                fontWeight = FontWeight.Bold,
                color = labelColor
            )
        },
        leadingIcon = {
            if (isAiSuggestion) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = labelColor
                )
            }
        },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor,
            labelColor = labelColor,
            leadingIconContentColor = labelColor,
            disabledLeadingIconContentColor = labelColor,
            disabledContainerColor = containerColor,
            disabledLabelColor = labelColor
        )
    )

}