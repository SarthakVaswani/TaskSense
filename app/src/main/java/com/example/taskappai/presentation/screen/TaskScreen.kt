package com.example.taskappai.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskappai.presentation.components.ErrorDialog
import com.example.taskappai.presentation.components.PriorityFilter
import com.example.taskappai.presentation.components.ShimmerTaskList
import com.example.taskappai.presentation.components.TaskBottomSheet
import com.example.taskappai.presentation.components.TaskList
import com.example.taskappai.presentation.components.TaskPromptBottomSheet

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TaskScreen(modifier: Modifier = Modifier, viewModel: TaskViewModel) {

    val tasks by viewModel.filteredTasks.collectAsState()
    val isInitialLoading by viewModel.isInitialLoading.collectAsState()
    val isAddingTask by viewModel.isAddingTask.collectAsState()
    val isDialogLoading by viewModel.isDialogLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val aiSuggestedPriority by viewModel.aiSuggestedPriority.collectAsState()
    val selectedTask by viewModel.selectedTask.collectAsState()
    val selectedPriority by viewModel.selectedPriority.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val showPromptSheet by viewModel.showPromptSheet.collectAsState()
    val isProcessingPrompt by viewModel.isProcessingPrompt.collectAsState()

    Box(modifier = modifier.fillMaxSize(),) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
        ) {
            PriorityFilter(
                selectedPriority = selectedPriority,
                onPrioritySelected = viewModel::setPriorityFilter,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            AnimatedContent(
                targetState = isInitialLoading to tasks.isEmpty(),
                transitionSpec = {
                    fadeIn() with fadeOut()
                },
                modifier = modifier,
                label = "taskList"
            ) {
                when {
                    isInitialLoading -> ShimmerTaskList(tasksSize = if (tasks.isEmpty()) 1 else tasks.size)
                    tasks.isEmpty() -> {
                        EmptyStateScreen(
                            selectedPriority = selectedPriority,
                            onAddClick = { viewModel.showPromptBottomSheet() }
                        )
                    }

                    else -> {
                        TaskList(tasks, viewModel, isAddingTask)

                    }
                }
            }
        }

    }

    val fabScale by animateFloatAsState(
        targetValue = if (isInitialLoading) 0.8f else 1f,
        label = "fab"
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        AnimatedVisibility(
            visible = tasks.isNotEmpty()
        ) {
            FloatingActionButton(
                onClick = { viewModel.showPromptBottomSheet() },
                modifier = Modifier
                    .padding(16.dp)
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp)
                    .scale(fabScale),
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor =  MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    }

    selectedTask?.let { task ->
        TaskBottomSheet(
            isVisible = true,
            onDismiss = {
                viewModel.clearSelectedTask()
                viewModel.clearAISuggestion()
            },
            onTaskAction = { title, description, dueDate, priority, category ->
                viewModel.updateTask(
                    task.copy(
                        title = title,
                        description = description,
                        dueDate = dueDate,
                        priority = priority,
                        category = category
                    )
                )
            },
            isLoading = isDialogLoading,
            aiSuggestedPriority = aiSuggestedPriority,
            onDescriptionChange = { description ->
                viewModel.getAISuggestedPriority(description)
            },
            task = task,
            isEditMode = true
        )
    }
    // Show TaskPromptBottomSheet
    TaskPromptBottomSheet(
        isVisible = showPromptSheet,
        onDismiss = { viewModel.hidePromptBottomSheet() },
        onPromptSubmit = { prompt -> viewModel.processTaskPrompt(prompt) },
        isLoading = isProcessingPrompt
    )
//    if (showBottomSheet) {
//        TaskPromptBottomSheet(
//            isVisible = isProcessingPrompt,
//            onDismiss = { showBottomSheet = false },
//            onPromptSubmit = {prompt ->
//                viewModel.processTaskPrompt(prompt)
//                showBottomSheet = false
//                viewModel.clearAISuggestion()
//            },
//            isLoading = isDialogLoading,
//        )
//    }


    error?.let { errorMessage ->
        ErrorDialog(
            error = errorMessage,
            onDismiss = { viewModel.clearError() }
        )
    }
}

