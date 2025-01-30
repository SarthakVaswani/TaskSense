package com.example.taskappai.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskappai.domain.model.Priority
import com.example.taskappai.ui.theme.Black
import com.example.taskappai.ui.theme.White

@Composable
fun EmptyStateScreen(
    selectedPriority: Priority?,
    onAddClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (selectedPriority != null)
                "No ${selectedPriority.name} priority tasks"
            else
                "No tasks added",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            modifier = Modifier
                .height(70.dp)
                .width(200.dp),
            onClick = onAddClick,
            colors = ButtonDefaults.textButtonColors(
                containerColor = Black,
                contentColor = White
            )
        ) {
            Text(
                "Add new task",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}