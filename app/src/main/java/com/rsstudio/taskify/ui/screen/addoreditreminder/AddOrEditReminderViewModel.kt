package com.rsstudio.taskify.ui.screen.addoreditreminder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsstudio.taskify.domain.db.AddReminderToDBUseCase
import com.rsstudio.taskify.domain.db.DeleteReminderByIdToDBUseCase
import com.rsstudio.taskify.domain.db.GetReminderByIdUseCase
import com.rsstudio.taskify.domain.db.UpdateReminderDBUseCase
import com.rsstudio.taskify.jobs.ReminderWorker
import com.rsstudio.taskify.ui.navigation.AppArgs
import com.rsstudio.taskify.ui.screen.addoreditreminder.AddOrEditReminderViewModel.Companion.DAILY
import com.rsstudio.taskify.ui.screen.addoreditreminder.AddOrEditReminderViewModel.Companion.EVERY_15_MINUTES
import com.rsstudio.taskify.ui.screen.addoreditreminder.AddOrEditReminderViewModel.Companion.HOURLY
import com.rsstudio.taskify.util.TimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class AddOrEditReminderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addReminderToDBUseCase: AddReminderToDBUseCase,
    private val updateReminderDBUseCase: UpdateReminderDBUseCase,
    private val deleteReminderByIdToDBUseCase: DeleteReminderByIdToDBUseCase,
    private val getReminderByIdUseCase: GetReminderByIdUseCase
) : ViewModel(
) {
    private val reminderId: String = savedStateHandle[AppArgs.ARG_REMINDER_ID] ?: ""

    var uiState by mutableStateOf(AddOrEditReminderUiState())
        private set

    var uiSideEffect by mutableStateOf<AddOrEditReminderSideEffects>(AddOrEditReminderSideEffects.NoEffect)
        private set

    init {
        uiState = uiState.copy(editingReminder = reminderId.isNotEmpty(), reminderId = reminderId)
        setData()
    }


    private fun setData() {
        if (!uiState.editingReminder) return
        viewModelScope.launch {
            getReminderByIdUseCase(reminderId).catch { }.collect {
                uiState = uiState.copy(
                    titleTextFieldValue = TextFieldValue(text = it.title),
                    descriptionTextFieldValue = TextFieldValue(text = it.description),
                    selectedReminderInterval = it.reminderInterval,
                    timeStamp = it.timeStamp,
                    fromApi = it.fromApi
                )
            }
        }
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
                deleteReminder()
            }

            AddOrEditReminderUIEvent.OnAddOrUpdateReminder -> {
                if (uiState.validateFields()) {
                    addOrUpdateReminder()
                }
            }
        }
    }

    fun resetUiSideEffect() {
        uiSideEffect = AddOrEditReminderSideEffects.NoEffect
    }

    private fun addOrUpdateReminder() {
        viewModelScope.launch {
            if (uiState.editingReminder) {
                updateReminderDBUseCase(
                    id = reminderId,
                    title = uiState.titleTextFieldValue.text,
                    description = uiState.descriptionTextFieldValue.text,
                    reminderInterval = uiState.selectedReminderInterval

                )
            } else {
                addReminderToDBUseCase(
                    title = uiState.titleTextFieldValue.text,
                    description = uiState.descriptionTextFieldValue.text,
                    reminderInterval = uiState.selectedReminderInterval
                )
            }
            uiSideEffect = AddOrEditReminderSideEffects.OnDone()
        }
    }

    private fun deleteReminder() {
        viewModelScope.launch {
            deleteReminderByIdToDBUseCase(reminderId)
            uiSideEffect = AddOrEditReminderSideEffects.OnDone(true)
        }
    }

    companion object {
        const val EVERY_15_MINUTES = "Every 15 minutes"
        const val HOURLY = "Hourly"
        const val DAILY = "Daily"
    }
}

data class AddOrEditReminderUiState(
    val titleTextFieldValue: TextFieldValue = TextFieldValue(),
    val descriptionTextFieldValue: TextFieldValue = TextFieldValue(),
    val selectedReminderInterval: String = "",
    val editingReminder: Boolean = false,
    val reminderId: String = "",
    val timeStamp: Long = 0L,
    val fromApi: Boolean = false
) {
    fun validateFields() =
        titleTextFieldValue.text.isNotEmpty() || descriptionTextFieldValue.text.isNotEmpty()

    fun getTimeInterval() = when (selectedReminderInterval) {
        EVERY_15_MINUTES -> {
            TimeUnit.MINUTES
        }

        HOURLY -> {
            TimeUnit.HOURS
        }

        DAILY -> {
            TimeUnit.DAYS
        }

        else -> {
            TimeUnit.MINUTES
        }
    }

    fun getIntervalUnit() = when (selectedReminderInterval) {
        EVERY_15_MINUTES -> {
            15L
        }

        HOURLY -> {
            1L
        }

        DAILY -> {
            1L
        }

        else -> {
            15L
        }
    }

    fun getTime() = if (timeStamp == 0L) TimeUtil.getTimeStamp() else timeStamp
}

sealed interface AddOrEditReminderUIEvent {
    data class OnDescriptionTextChanged(val textFieldValue: TextFieldValue) :
        AddOrEditReminderUIEvent

    data class OnTitleTextChanged(val textFieldValue: TextFieldValue) : AddOrEditReminderUIEvent
    data class OnSelectReminderInterval(val interval: String) : AddOrEditReminderUIEvent
    data object OnDeleteReminder : AddOrEditReminderUIEvent
    data object OnAddOrUpdateReminder : AddOrEditReminderUIEvent
}

sealed interface AddOrEditReminderSideEffects {
    data object NoEffect : AddOrEditReminderSideEffects
    data class OnDone(val delete: Boolean = false) : AddOrEditReminderSideEffects
}
