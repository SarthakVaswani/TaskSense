package com.example.taskappai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskappai.util.Converters

@Database(
    entities = [TaskEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TaskDB : RoomDatabase() {
    abstract val taskDao: TaskDao
}