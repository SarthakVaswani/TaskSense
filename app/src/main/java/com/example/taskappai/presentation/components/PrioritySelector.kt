package com.example.taskappai.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.ui.theme.*

@Composable
fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Priority.entries.forEach { priority ->
            val (containerColor, selectedColor, textColor) = when (priority) {
                Priority.High -> Triple(LightPink, IconPink, IconPink)
                Priority.Medium -> Triple(PastelYellow, IconYellow, IconYellow)
                Priority.Low -> Triple(LightBlue, IconBlue, IconBlue)
            }
            
            FilterChip(
                selected = priority == selectedPriority,
                onClick = { onPrioritySelected(priority) },
                label = { 
                    Text(
                        priority.name,
                        color = if (priority == selectedPriority) White else textColor,
                        fontWeight = FontWeight.Medium
                    ) 
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = selectedColor,
                    containerColor = containerColor,
                    labelColor = if (priority == selectedPriority) White else textColor
                )
            )
        }
    }
}