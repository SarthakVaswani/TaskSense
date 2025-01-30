package com.example.taskappai.util

import androidx.room.TypeConverter
import com.example.taskappai.domain.model.Priority

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority?): String? {
        return priority?.name
    }

    @TypeConverter
    fun toPriority(value: String?): Priority? {
        return value?.let { Priority.valueOf(it) }
    }
}