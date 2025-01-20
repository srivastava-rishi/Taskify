package com.rsstudio.taskify.ui.navigation

import androidx.navigation.NavController
import com.rsstudio.taskify.common.extension.withArg
import com.rsstudio.taskify.ui.navigation.actions.AddOrEditReminderScreenActions
import com.rsstudio.taskify.ui.navigation.actions.ReminderListScreenActions

class AppNavigationActions(
    private val navController: NavController,
    private val onFinish: () -> Unit
) {

    private fun back() {
        navController.popBackStack()
    }

    private fun finishActivity() {
        onFinish()
    }

    fun navigateFromReminderListScreen(actions: ReminderListScreenActions) {
        when (actions) {
            is ReminderListScreenActions.OpenAddOrEditReminderScreen -> {
                navController.navigate(
                    AppScreen.AddOrEditReminderScreen.name
                        .withArg(AppArgs.ARG_REMINDER_ID, actions.id)
                )
            }
        }
    }

    fun navigateFromAddOrEditReminderScreen(actions: AddOrEditReminderScreenActions) {
        when (actions) {
            AddOrEditReminderScreenActions.OnBack -> {
                back()
            }
        }
    }
}