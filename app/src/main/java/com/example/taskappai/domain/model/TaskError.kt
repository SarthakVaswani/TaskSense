package com.example.taskappai.domain.model

import android.database.sqlite.SQLiteException

sealed class TaskError(val message: String) {
    data class NetworkError(val throwable: Throwable) :
        TaskError("Network Error : ${throwable.message}")

    data class DatabaseError(val throwable: Throwable) :
        TaskError("Database Error : ${throwable.message}")

    data class ValidationError(val field: String) :
        TaskError("Invalid $field")

    data class AIServiceError(val throwable: Throwable) :
        TaskError("AI Service error: ${throwable.message}")

    object NoInternetConnection :
        TaskError("No internet connection")

    companion object {
        fun fromThrowable(throwable: Throwable): TaskError {
            return when (throwable) {
                is java.net.UnknownHostException -> NoInternetConnection
                is retrofit2.HttpException -> NetworkError(throwable)
                is SQLiteException -> DatabaseError(throwable)
                else -> NetworkError(throwable)
            }
        }
    }
}