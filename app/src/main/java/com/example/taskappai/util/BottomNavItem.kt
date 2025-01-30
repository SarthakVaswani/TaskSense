package com.example.taskappai.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Task : Screen("task_screen")
    data object Calendar : Screen("calendar_screen")
    data object Settings : Screen("settings_screen")
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Tasks : BottomNavItem(
        route = Screen.Task.route,
        title = "Tasks",
        icon = Icons.Default.List
    )

    data object Calendar : BottomNavItem(
        route = Screen.Calendar.route,
        title = "Calendar",
        icon = Icons.Default.DateRange
    )

    data object Settings : BottomNavItem(
        route = Screen.Settings.route,
        title = "Settings",
        icon = Icons.Default.Settings
    )

}