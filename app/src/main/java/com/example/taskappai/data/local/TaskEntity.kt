package com.example.taskappai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.domain.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Priority,
    val category: String,
    val isCompleted: Boolean = false,
    val aiSuggestedPriority: Priority? = null
){
    fun toTask(): Task = Task(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        priority = priority,
        category = category,
        isCompleted = isCompleted,
        aiSuggestedPriority = aiSuggestedPriority
    )
}
