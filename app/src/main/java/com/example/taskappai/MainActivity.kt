package com.example.taskappai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskappai.presentation.screen.TaskScreen
import com.example.taskappai.presentation.screen.TaskViewModel
import com.example.taskappai.ui.theme.TaskAppAiTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    private val viewModel: TaskViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            TaskAppAiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    TaskScreen(
                        modifier = Modifier.padding(paddingValues),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}