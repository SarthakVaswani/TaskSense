package com.example.taskappai.domain.repository

import com.example.taskappai.data.local.TaskDao
import com.example.taskappai.data.local.TaskEntity
import com.example.taskappai.data.remote.api.DeepSeekRepository
import com.example.taskappai.domain.model.ExtractedTaskDetails
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val deepSeekRepository: DeepSeekRepository
) : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toTask() }
        }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTasksById(id)?.toTask()
    }

    override suspend fun insertTask(task: Task) {
        try {
            taskDao.insertTask(
                TaskEntity(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    dueDate = task.dueDate,
                    priority = task.priority,
                    category = task.category,
                    isCompleted = task.isCompleted,
                    aiSuggestedPriority = task.aiSuggestedPriority
                )
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                dueDate = task.dueDate,
                priority = task.priority,
                category = task.category,
                isCompleted = task.isCompleted,
                aiSuggestedPriority = task.aiSuggestedPriority
            )
        )
    }

    override suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean) {
        taskDao.updateTaskCompletion(taskId, isCompleted)
    }

    override suspend fun getSuggestedPriority(taskDescription: String): Result<Priority> {
        return try {
            deepSeekRepository.getPrioritySuggestion(taskDescription)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                dueDate = task.dueDate,
                priority = task.priority,
                category = task.category,
                isCompleted = task.isCompleted,
                aiSuggestedPriority = task.aiSuggestedPriority
            )
        )
    }

    override suspend fun extractTaskFromPrompt(prompt: String): Result<ExtractedTaskDetails> {
        return try {
            deepSeekRepository.extractTaskDetails(prompt)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}