package com.example.taskappai.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.taskappai.ui.theme.*

@Composable
fun shimmerEffect(): Brush {
    val shimmerColors = listOf(
        LightGray.copy(alpha = 0.6f),
        LightGray.copy(alpha = 0.2f),
        LightGray.copy(alpha = 0.6f),
    )

    val transitions = rememberInfiniteTransition(label = "Shimmer")
    val transitionAnim = transitions.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Shimmer"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = transitionAnim.value, y = transitionAnim.value)
    )
}

fun Modifier.shimmerEffect(): Modifier = composed {
    background(com.example.taskappai.presentation.components.shimmerEffect())
}

@Composable
fun TaskItemShimmer(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox shimmer
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title shimmer
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Description shimmer
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Priority chip shimmer
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun ShimmerTaskList(tasksSize: Int = 1) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasksSize) {
            TaskItemShimmer()
        }
    }
}