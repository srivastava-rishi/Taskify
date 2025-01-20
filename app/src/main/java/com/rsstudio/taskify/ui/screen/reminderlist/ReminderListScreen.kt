package com.rsstudio.taskify.ui.screen.reminderlist

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsstudio.taskify.component.loader.DefaultCircularIndefiniteBar
import com.rsstudio.taskify.component.loader.ErrorMessage
import com.rsstudio.taskify.common.alias.AppString
import com.rsstudio.taskify.ui.navigation.actions.ReminderListScreenActions
import androidx.lifecycle.Lifecycle
import com.rsstudio.taskify.common.PBLifeCycleCallBack
import com.rsstudio.taskify.ui.theme.Purple40
import com.rsstudio.taskify.ui.theme.black50
import com.rsstudio.taskify.ui.theme.grey
import com.rsstudio.taskify.ui.theme.ht1
import com.rsstudio.taskify.ui.theme.lightBlack
import com.rsstudio.taskify.ui.theme.slateGrey
import com.rsstudio.taskify.ui.theme.subTitle
import com.rsstudio.taskify.ui.theme.white


@Composable
fun ReminderListScreen(
    viewModel: ReminderListViewModel = hiltViewModel(),
    onAction: (reminderListScreenActions: ReminderListScreenActions) -> Unit
) {
    val permissionGranted = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted.value = isGranted
    }
    Scaffold(
        floatingActionButton = {
            if (!viewModel.uiState.isLoading()) {
                FloatingActionButton(onClick = {
                    onAction(ReminderListScreenActions.OpenAddOrEditReminderScreen(""))
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "addReminder")
                }
            }
        }
    ) {
        ReminderContent(
            modifier = Modifier.padding(it),
            uiState = viewModel.uiState,
            editReminder = {
                onAction(ReminderListScreenActions.OpenAddOrEditReminderScreen(it))
            },
            onEvent = viewModel::onEvent
        )
    }

    PBLifeCycleCallBack { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.onEvent(ReminderListUIEvent.OnResumeEvent)
            }

            else -> {
                // stub
            }
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            permissionGranted.value = true
        }
    }
}

@Composable
fun ReminderContent(
    modifier: Modifier = Modifier,
    uiState: ReminderListUiState,
    editReminder: (String) -> Unit,
    onEvent: (ReminderListUIEvent) -> Unit
) {
    when (uiState.screenState) {
        ScreenState.ERROR -> {
            ErrorMessage(errorMessage = stringResource(id = AppString.no_data_found))
        }

        ScreenState.NONE -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = modifier
                    .background(slateGrey)
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.list.size) { index ->
                    ReminderCard(
                        reminder = uiState.list[index],
                        editReminder = {
                            editReminder(it)
                        },
                        onLongClick = {
                            onEvent(ReminderListUIEvent.OnLongClick(it))
                        }
                    )
                }
            }
        }

        else -> {
            DefaultCircularIndefiniteBar()
        }
    }
}

@Composable
fun ReminderCard(
    reminder: Reminder,
    editReminder: (String) -> Unit,
    onLongClick: (Reminder) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, grey, RoundedCornerShape(12.dp))
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        editReminder(reminder.id)
                    },
                    onLongPress = {
                        onLongClick(reminder)
                    }
                )
            }
    ) {
        if (reminder.title.isNotEmpty()) {
            Text(
                text = reminder.title,
                style = MaterialTheme.typography.ht1.copy(fontSize = 12.sp, color = lightBlack)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        if (reminder.description.isNotEmpty()) {
            Text(
                text = reminder.description,
                style = MaterialTheme.typography.subTitle.copy(fontSize = 10.sp, color = black50)
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
        if (reminder.interval.isNotEmpty()) {
            ReminderChip(reminder.interval)
        }
    }
}

@Composable
fun ReminderChip(reminderInterval: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Purple40)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = reminderInterval,
            style = MaterialTheme.typography.subTitle.copy(fontSize = 12.sp, color = white)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderListPreview() {
    val reminders = listOf(
        Reminder(
            "1",
            title = "Morning Walk",
            description = "Walk in the park at 6 AM.",
            interval = "Hourly",
            fromApi = false
        ),
        Reminder(
            "2",
            title = "Buy Groceries",
            description = "Don't forget to buy milk, eggs, and bread.",
            interval = "Every 15 minutes",
            fromApi = false
        ),
        Reminder(
            "3",
            title = "",
            description = "Yoga at 7 PM in the living room.",
            interval = "Daily",
            fromApi = false
        ),
        Reminder(
            "4",
            title = "Meeting with Client",
            description = "Discuss project details at 11 AM.",
            interval = "Weekly",
            fromApi = false
        ),
        Reminder(
            "5",
            title = "",
            description = "Discuss project details at 11 AM.",
            interval = "Weekly",
            fromApi = false
        ),
        Reminder(
            "6",
            title = "a",
            description = "",
            interval = "Weekly",
            fromApi = false
        )
    )
    ReminderContent(
        uiState = ReminderListUiState(),
        editReminder = {},
        onEvent = {}
    )
}



