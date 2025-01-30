package com.example.taskappai.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.domain.model.Task
import com.example.taskappai.domain.model.TaskError
import com.example.taskappai.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class TaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    private val _selectedPriority = MutableStateFlow<Priority?>(null)
    val selectedPriority = _selectedPriority.asStateFlow()

    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading = _isInitialLoading.asStateFlow()

    private val _isAddingTask = MutableStateFlow(false)
    val isAddingTask = _isAddingTask.asStateFlow()

    private val _isDialogLoading = MutableStateFlow(false)
    val isDialogLoading = _isDialogLoading.asStateFlow()

    private val _error = MutableStateFlow<TaskError?>(null)
    val error = _error.asStateFlow()

    private val _aiSuggestedPriority = MutableStateFlow<Priority?>(null)
    val aiSuggestedPriority = _aiSuggestedPriority.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask = _selectedTask.asStateFlow()

    private val _isProcessingPrompt = MutableStateFlow(false)
    val isProcessingPrompt = _isProcessingPrompt.asStateFlow()

    private val _showPromptSheet = MutableStateFlow(false)
    val showPromptSheet = _showPromptSheet.asStateFlow()


    val filteredTasks = combine(_tasks, _selectedPriority) { tasks, priority ->
        when (priority) {
            null -> tasks
            else -> tasks.filter { it.priority == priority }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        getTasks()
    }


    private fun getTasks() {
        viewModelScope.launch {
            _isInitialLoading.value = true  // Start loading
            try {
                taskRepository.getAllTasks()
                    .onEach { tasks ->
                        _tasks.value = tasks
                        _isInitialLoading.value = false  // Stop loading after getting tasks
                    }
                    .launchIn(viewModelScope)
            } catch (e: Exception) {
                _error.value = TaskError.fromThrowable(e)
                _isInitialLoading.value = false  // Stop loading on error
            }
        }
    }

    fun addTask(
        title: String,
        description: String,
        dueDate: Long,
        priority: Priority,
        category: String
    ) {

        if (title.isBlank()) {
            _error.value = TaskError.ValidationError("title")
            return
        }
        if (description.isBlank()) {
            _error.value = TaskError.ValidationError("description")
        }
        viewModelScope.launch {
            _isAddingTask.value = true
            try {
                val aiPriority = taskRepository.getSuggestedPriority(description).getOrNull()
                val task = Task(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    priority = priority,
                    category = category,
                    isCompleted = false,
                    aiSuggestedPriority = aiPriority
                )
                taskRepository.insertTask(task)
                _error.value = null
            } catch (e: Exception) {
                _error.value = TaskError.fromThrowable(e)
            } finally {
                _isAddingTask.value = false
            }
        }
    }

    fun toggleTaskCompletion(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTaskCompletion(taskId, isCompleted)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun getAISuggestedPriority(description: String) {
        if (description.length < 10) {
            return
        }
        viewModelScope.launch {
            _isDialogLoading.value = true
            try {
                taskRepository.getSuggestedPriority(description)
                    .onSuccess { priority ->
                        _aiSuggestedPriority.value = priority
                    }
                    .onFailure { e ->
                        _error.value = TaskError.AIServiceError(e)
                    }

            } catch (e: Exception) {
                _error.value = TaskError.fromThrowable(e)
            } finally {
                _isDialogLoading.value = false

            }
        }
    }

    fun clearAISuggestion() {
        _aiSuggestedPriority.value = null
    }

    fun selectTask(task: Task) {
        _selectedTask.value = task
        getAISuggestedPriority(task.description)
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                _isDialogLoading.value = true
                taskRepository.updateTask(task)

                // Update local list
                val currentTasks = _tasks.value.toMutableList()
                val index = currentTasks.indexOfFirst { it.id == task.id }
                if (index != -1) {
                    currentTasks[index] = task
                    _tasks.value = currentTasks
                }

                // Clear selected task
                _selectedTask.value = null
            } catch (e: Exception) {
                _error.value = TaskError.fromThrowable(e)
            } finally {
                _isDialogLoading.value = false
            }
        }
    }


    fun clearSelectedTask() {
        _selectedTask.value = null
        clearAISuggestion()
    }

    fun clearError() {
        _error.value = null
    }

    fun updatePriority(taskId: Int, newPriority: Priority) {
        viewModelScope.launch {
            try {
                val task = tasks.value.find { it.id == taskId }
                task?.let {
                    val updatedTask = it.copy(
                        priority = newPriority,
                        aiSuggestedPriority = null
                    )
                    taskRepository.updateTask(updatedTask)
                }
            } catch (e: Exception) {
                _error.value = TaskError.fromThrowable(e)
            }
        }
    }

    fun setPriorityFilter(priority: Priority?) {
        _selectedPriority.value = priority
    }

    fun showPromptBottomSheet() {
        _showPromptSheet.value = true
    }

    fun hidePromptBottomSheet() {
        _showPromptSheet.value = false
    }

    fun processTaskPrompt(prompt: String) {
        viewModelScope.launch {
            _isProcessingPrompt.value = true
            try {
                taskRepository.extractTaskFromPrompt(prompt)
                    .onSuccess { taskDetails ->
                        addTask(
                            title = taskDetails.title,
                            description = taskDetails.description,
                            dueDate = taskDetails.dueDate ?: 0L,
                            priority = taskDetails.priority,
                            category = taskDetails.category
                        )
                        hidePromptBottomSheet()
                    }
                    .onFailure { e ->
                        _error.value = TaskError.AIServiceError(e)
                    }
            } finally {
                _isProcessingPrompt.value = false
            }
        }
    }
}