package com.example.taskappai.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.ui.theme.*

@Composable
fun PriorityFilter(
    modifier: Modifier = Modifier,
    selectedPriority: Priority?,
    onPrioritySelected: (Priority?) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // All Priority Chip
        FilterChip(
            selected = selectedPriority == null,
            onClick = { onPrioritySelected(null) },
            label = { 
                Text(
                    "All",
                    fontWeight = FontWeight.Medium,
                    color = if (selectedPriority == null) White else TextPrimary
                ) 
            },
            leadingIcon = {
                if (selectedPriority == null) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "All Priority",
                        modifier = Modifier.size(16.dp),
                        tint = White
                    )
                }
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Black,
                containerColor = LightGray
            )
        )

        // Priority Filters
        Priority.entries.forEach { priority ->
            val (containerColor, selectedColor, textColor) = when (priority) {
                Priority.High -> Triple(LightPink, IconPink, IconPink)
                Priority.Medium -> Triple(PastelYellow, IconYellow, IconYellow)
                Priority.Low -> Triple(LightBlue, IconBlue, IconBlue)
            }
            
            FilterChip(
                selected = selectedPriority == priority,
                onClick = { onPrioritySelected(priority) },
                label = { 
                    Text(
                        priority.name,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedPriority == priority) White else textColor
                    ) 
                },
                leadingIcon = {
                    if (selectedPriority == priority) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = priority.name,
                            modifier = Modifier.size(16.dp),
                            tint = White
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = selectedColor,
                    containerColor = containerColor
                )
            )
        }
    }
}