package com.rsstudio.taskify.ui.screen.reminderlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ReminderListViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(ReminderListUiState())
        private set
}


data class ReminderListUiState(
    val list: List<Reminder> = emptyList()
) {
    fun data() = listOf(
        Reminder(
            title = "Morning Walk",
            description = "Walk in the park at 6 AM.",
            interval = "Hourly"
        ),
        Reminder(
            title = "Buy Groceries",
            description = "Don't forget to buy milk, eggs, and bread.",
            interval = "Every 15 minutes"
        ),
        Reminder(
            title = "",
            description = "Yoga at 7 PM in the living room.",
            interval = "Daily"
        ),
        Reminder(
            title = "Meeting with Client",
            description = "Discuss project details at 11 AM.",
            interval = "Weekly"
        ),
        Reminder(
            title = "",
            description = "Discuss project details at 11 AM.",
            interval = "Weekly"
        ),
        Reminder(
            title = "a",
            description = "",
            interval = "Weekly"
        )
    )
}

data class Reminder(
    val title: String,
    val description: String,
    val interval: String
)