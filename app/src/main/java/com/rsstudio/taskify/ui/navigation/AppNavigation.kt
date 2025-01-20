package com.rsstudio.taskify.ui.navigation

import com.rsstudio.taskify.common.extension.addRouteArgument


/**
 * Args irrespective of screens are kept here
 */

object AppArgs {
    const val ARG_REMINDER_ID = "news_Id"
}

sealed class AppScreen(val name: String, val route: String) {
    data object ReminderListScreen : AppScreen("reminderList", "reminderList")
    data object AddOrEditReminderScreen : AppScreen(
        "addOrEditReminder", "addOrEditReminder"
            .addRouteArgument(AppArgs.ARG_REMINDER_ID)
    )
}