package com.example.taskappai.data

data class ChatRequest(
    val model: String = "deepseek-chat",
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 500
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val message: Message,
    val finish_reason: String
)