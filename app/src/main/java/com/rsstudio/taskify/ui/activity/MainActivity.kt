package com.rsstudio.taskify.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.rsstudio.taskify.ui.navigation.AppNavGraph
import com.rsstudio.taskify.ui.navigation.AppNavigationActions
import com.rsstudio.taskify.ui.theme.TaskifyTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskifyTheme {
                ReminderApp(onFinish = {
                    finish()
                })
            }
        }
    }
}

@Composable
fun ReminderApp(
    onFinish: () -> Unit
) {
    val navController = rememberNavController()
    val navActions = remember(navController) {
        AppNavigationActions(navController, onFinish)
    }
    AppNavGraph(
        navController = navController,
        navActions = navActions
    )
}
