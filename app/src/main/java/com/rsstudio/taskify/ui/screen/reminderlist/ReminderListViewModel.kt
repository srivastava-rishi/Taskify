package com.rsstudio.taskify.ui.screen.reminderlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsstudio.taskify.domain.db.GetReminderDataFromDBUseCase
import com.rsstudio.taskify.domain.local.TextToSpeechUseCase
import com.rsstudio.taskify.domain.mapper.toUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderListViewModel @Inject constructor(
    private val getReminderDataFromDBUseCase: GetReminderDataFromDBUseCase,
    private val textToSpeechUseCase: TextToSpeechUseCase
) : ViewModel() {

    var uiState by mutableStateOf(ReminderListUiState())
        private set

    init {
        fetchAllReminders()
    }

    private fun fetchAllReminders() {
        viewModelScope.launch {
            getReminderDataFromDBUseCase().catch {
                uiState = uiState.copy(
                    screenState = ScreenState.ERROR
                )
            }.collect {
                uiState = if (it.isNotEmpty()) {
                    uiState.copy(
                        screenState = ScreenState.NONE,
                        list = it
                    )
                } else {
                    uiState.copy(
                        screenState = ScreenState.ERROR
                    )
                }
            }
        }
    }

    fun onEvent(event: ReminderListUIEvent) {
        when (event) {
            ReminderListUIEvent.OnResumeEvent -> {
                fetchAllReminders()
            }

            is ReminderListUIEvent.OnLongClick -> {
                val text = event.reminder.title + event.reminder.description
                textToSpeechUseCase.speak(text)
            }
        }
    }
}


data class ReminderListUiState(
    val screenState: ScreenState = ScreenState.LOADING,
    val list: List<Reminder> = emptyList()
) {
    fun isLoading() = screenState == ScreenState.LOADING
}

sealed interface ReminderListUIEvent {
    data object OnResumeEvent : ReminderListUIEvent
    data class OnLongClick(val reminder: Reminder) : ReminderListUIEvent
}

data class Reminder(
    val id: String,
    val title: String,
    val description: String,
    val interval: String,
    val fromApi: Boolean
)

enum class ScreenState {
    NONE,
    LOADING,
    ERROR
}