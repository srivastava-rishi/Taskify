package com.rsstudio.taskify.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.rsstudio.taskify.ui.common.extension.animatedComposable
import com.rsstudio.taskify.ui.screen.addoreditreminder.AddOrEditReminderScreen
import com.rsstudio.taskify.ui.screen.reminderlist.ReminderListScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = AppScreen.ReminderListScreen.route,
    navActions: AppNavigationActions,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        animatedComposable(AppScreen.ReminderListScreen.route) {
            ReminderListScreen(
                onAction = navActions::navigateFromReminderListScreen
            )
        }
        animatedComposable(
            route = AppScreen.AddOrEditReminderScreen.route,
            arguments = listOf(
                navArgument(AppArgs.ARG_REMINDER_ID) { type = NavType.StringType }
            )
        ) {
            AddOrEditReminderScreen(onAction = navActions::navigateFromAddOrEditReminderScreen)
        }
    }
}