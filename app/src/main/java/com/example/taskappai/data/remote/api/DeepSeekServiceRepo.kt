package com.example.taskappai.data.remote.api

import android.util.Log
import com.example.taskappai.data.ChatRequest
import com.example.taskappai.data.Message
import com.example.taskappai.domain.model.ExtractedTaskDetails
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.util.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DeepSeekRepository(
    private val api: DeepSeekService
) {
    companion object{
        private val TAG = "DeepSeekRepository"
    }


    suspend fun getPrioritySuggestion(taskDescription: String): Result<Priority> {
        return try {
            val request = ChatRequest(
                messages = listOf(
                    Message(
                        role = "system",
                        content = "You are a task prioritization expert. Respond with exactly one word: HIGH, MEDIUM, or LOW."
                    ),
                    Message(
                        role = "user",
                        content = "Based on this task description, what should be the priority? Task: $taskDescription"
                    )
                )
            )

            val response = api.getChatCompletion(
                apiKey = "Bearer ${Constants.API_KEY}",
                request = request
            )

            val suggestion = response.choices.firstOrNull()?.message?.content
                ?: throw Exception("No suggestion received")

            val priority = when (suggestion) {
                "HIGH" -> Priority.High
                "MEDIUM" -> Priority.Medium
                "LOW" -> Priority.Low
                else -> throw Exception("Invalid priority suggestion: $suggestion")
            }

            Result.success(priority)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun extractTaskDetails(userPrompt: String): Result<ExtractedTaskDetails> {
        return try {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val today = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val tomorrow = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val dayAfterTomorrow = dateFormat.format(calendar.time)

            val request = ChatRequest(
                messages = listOf(
                    Message(
                        role = "system",
                        content = """
                            You are a task analyzer. Extract task details and respond with ONLY a valid JSON object like this example:
                            {"title":"Team Meeting","description":"Discuss project timeline","category":"Work","priority":"High","dueDate":"2024-03-15"}
                            
                            Rules:
                            - Keep title concise (max 50 chars)
                            - Put details in description
                            - Category should be one of: Work, Personal, Shopping, Health, Other
                            - Priority must be exactly: High, Medium, or Low
                            - Today is: $today
                            - Tomorrow is: $tomorrow
                            - Day after tomorrow is: $dayAfterTomorrow
                            - For dates, use yyyy-MM-dd format
                            - If no date mentioned, use empty string for dueDate
                            
                            Respond with ONLY the JSON object, no other text.
                        """.trimIndent()
                    ),
                    Message(
                        role = "user",
                        content = userPrompt
                    )
                )
            )

            val response = api.getChatCompletion(
                apiKey = "Bearer ${Constants.API_KEY}",
                request = request
            )

            val jsonResponse = response.choices.firstOrNull()?.message?.content?.trim()
                ?: throw Exception("No response received")

            Log.d(TAG, "Raw AI response: $jsonResponse")

            // Clean up the response to ensure it's valid JSON
            val cleanJson = jsonResponse
                .replace("\n", "")
                .trim()
                .let {
                    if (!it.startsWith("{")) it.substring(it.indexOf("{"))
                    else it
                }
                .let {
                    if (!it.endsWith("}")) it.substring(0, it.lastIndexOf("}") + 1)
                    else it
                }

            Log.d(TAG, "Cleaned JSON: $cleanJson")

            val gson = Gson()
            val responseData = gson.fromJson(cleanJson, JsonObject::class.java)

            Log.d(TAG, "Parsed JSON object: $responseData")


            val dueDate = responseData.get("dueDate")?.asString?.let { dateStr ->
                if (dateStr.isNotBlank()) {
                    try {
                        dateFormat.parse(dateStr)?.time
                    } catch (e: Exception) {
                        null
                    }
                } else null
            }

            val priority = try {
                Priority.valueOf(responseData.get("priority").asString.replaceFirstChar { it.uppercase() })
            } catch (e: Exception) {
                Priority.Medium
            }

            val extractedDetails = ExtractedTaskDetails(
                title = responseData.get("title").asString,
                description = responseData.get("description").asString,
                category = responseData.get("category").asString,
                priority = priority,
                dueDate = dueDate
            )

            Log.d(TAG, "Successfully extracted task details: $extractedDetails")

            Result.success(extractedDetails)
        } catch (e: Exception) {
            e.printStackTrace() // Add this for debugging
            Result.failure(e)
        }
    }

}