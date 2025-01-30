package com.example.taskappai.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.ui.theme.*
import com.example.taskappai.util.getPriorityColors

@Composable
fun AiSuggestionChip(
    priority: Priority,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "Scale"
    )

    val (containerColor, labelColor, _) = getPriorityColors(priority)

    AssistChip(
        onClick = onClick,
        label = { 
            Text(
                "AI Suggests: ${priority.name}",
                color = labelColor,
                fontWeight = FontWeight.Medium
            ) 
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = priority.name,
                tint = labelColor,
                modifier = Modifier.size(16.dp)
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor,
            labelColor = labelColor,
            leadingIconContentColor = labelColor
        ),
        modifier = modifier.scale(scale)
    )
}