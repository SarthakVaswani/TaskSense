package com.example.taskappai.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * from tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * from tasks WHERE id = :id")
    suspend fun getTasksById(id : Int) : TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task : TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean)

    @Update
    suspend fun updateTask(task: TaskEntity)
}