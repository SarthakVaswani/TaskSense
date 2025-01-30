package com.example.taskappai.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.ui.theme.*

@Composable
fun getPriorityColors(priority: Priority): Triple<Color, Color, Color> {
    val isDarkTheme = isSystemInDarkTheme()
    return when (priority) {
        Priority.High -> Triple(
            if (isDarkTheme) DarkHighPriority.copy(alpha = 0.2f) else LightHighPriority,
            if (isDarkTheme) DarkHighPriority else IconPink,
            if (isDarkTheme) White else IconPink
        )
        Priority.Medium -> Triple(
            if (isDarkTheme) DarkMediumPriority.copy(alpha = 0.2f) else LightMediumPriority,
            if (isDarkTheme) DarkMediumPriority else IconYellow,
            if (isDarkTheme) White else IconYellow
        )
        Priority.Low -> Triple(
            if (isDarkTheme) DarkLowPriority.copy(alpha = 0.2f) else LightLowPriority,
            if (isDarkTheme) DarkLowPriority else IconBlue,
            if (isDarkTheme) White else IconBlue
        )
    }
}
