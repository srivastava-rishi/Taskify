package com.rsstudio.taskify.ui.navigation.actions

/**
 * Add all you app screen actions here ====================================
 */


sealed class ReminderListScreenActions {
    data class OpenAddOrEditReminderScreen(val id: String) : ReminderListScreenActions()
}

sealed class AddOrEditReminderScreenActions {
    data object OnBack : AddOrEditReminderScreenActions()
}

