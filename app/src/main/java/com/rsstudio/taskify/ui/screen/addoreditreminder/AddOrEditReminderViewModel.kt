package com.rsstudio.taskify.ui.screen.addoreditreminder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.rsstudio.taskify.ui.navigation.AppArgs

class AddOrEditReminderViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(
) {
    private val reminderId: String = savedStateHandle[AppArgs.ARG_REMINDER_ID] ?: ""

    var uiState by mutableStateOf(AddOrEditReminderUiState())
        private set

    init {
        uiState = uiState.copy(editingReminder = reminderId.isNotEmpty())
    }


    fun onEvent(event: AddOrEditReminderUIEvent) {
        when (event) {
            is AddOrEditReminderUIEvent.OnDescriptionTextChanged -> {
                uiState = uiState.copy(descriptionTextFieldValue = event.textFieldValue)
            }

            is AddOrEditReminderUIEvent.OnTitleTextChanged -> {
                uiState = uiState.copy(titleTextFieldValue = event.textFieldValue)
            }

            is AddOrEditReminderUIEvent.OnSelectReminderInterval -> {
                uiState = uiState.copy(selectedReminderInterval = event.interval)
            }

            AddOrEditReminderUIEvent.OnDeleteReminder -> {

            }
        }
    }
}

data class AddOrEditReminderUiState(
    val titleTextFieldValue: TextFieldValue = TextFieldValue(),
    val descriptionTextFieldValue: TextFieldValue = TextFieldValue(),
    val selectedReminderInterval: String = "",
    val editingReminder: Boolean = false
)

sealed interface AddOrEditReminderUIEvent {
    data class OnDescriptionTextChanged(val textFieldValue: TextFieldValue) :
        AddOrEditReminderUIEvent

    data class OnTitleTextChanged(val textFieldValue: TextFieldValue) : AddOrEditReminderUIEvent
    data class OnSelectReminderInterval(val interval: String) : AddOrEditReminderUIEvent
    data object OnDeleteReminder : AddOrEditReminderUIEvent
}
