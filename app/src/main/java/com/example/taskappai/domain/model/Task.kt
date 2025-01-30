package com.example.taskappai.domain.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Priority,
    val category: String,
    val isCompleted: Boolean = false,
    val aiSuggestedPriority: Priority? = null
)

data class ExtractedTaskDetails(
    val title: String,
    val description: String,
    val category: String,
    val priority: Priority,
    val dueDate: Long? = null
)

enum class Priority {
    High, Medium, Low
}