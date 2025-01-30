package com.example.taskappai.data.remote.api

import com.example.taskappai.data.ChatRequest
import com.example.taskappai.data.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepSeekService {

    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") apiKey: String,
        @Body request : ChatRequest
    ) : ChatResponse
}