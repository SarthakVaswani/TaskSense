package com.example.taskappai.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskappai.domain.model.Task
import com.example.taskappai.presentation.screen.TaskViewModel

@Composable
 fun ColumnScope.TaskList(
    tasks: List<Task>,
    viewModel: TaskViewModel,
    isAddingTask: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks, key = { it.id }) { task ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                TaskItem(
                    task = task,
                    onTaskClick = { viewModel.selectTask(task) },
                    onTaskCheckedChanges = { isCompleted ->
                        viewModel.toggleTaskCompletion(task.id, isCompleted)
                    },
                    onDelete = { viewModel.deleteTask(task) },

                    onPriorityUpdate = { suggestedPriority ->
                        viewModel.updatePriority(
                            taskId = task.id,
                            newPriority = suggestedPriority
                        )
                    }

                )
            }


        }
        if (isAddingTask) {
            item(key = "loading") {
                TaskItemShimmer()
            }
        }
    }
}