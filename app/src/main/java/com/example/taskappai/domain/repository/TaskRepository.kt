package com.example.taskappai.domain.repository

import com.example.taskappai.domain.model.ExtractedTaskDetails
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks() : Flow<List<Task>>
    suspend fun getTaskById(id : Int) : Task?
    suspend fun insertTask(task : Task)
    suspend fun deleteTask(task: Task)
    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean)
    suspend fun getSuggestedPriority(taskDescription: String): Result<Priority>
    suspend fun updateTask(task: Task)
    suspend fun extractTaskFromPrompt(prompt: String): Result<ExtractedTaskDetails>

}