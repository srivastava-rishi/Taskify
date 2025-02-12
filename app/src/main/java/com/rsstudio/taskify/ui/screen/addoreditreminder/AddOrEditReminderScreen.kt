package com.rsstudio.taskify.ui.screen.addoreditreminder

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsstudio.taskify.common.alias.AppArray
import com.rsstudio.taskify.common.alias.AppDrawable
import com.rsstudio.taskify.common.alias.AppString
import com.rsstudio.taskify.common.extension.noRippleClickable
import com.rsstudio.taskify.component.alertdialog.ShowRationaleDialog
import com.rsstudio.taskify.component.alertdialog.ShowSettingsDialog
import com.rsstudio.taskify.component.chip.Chip
import com.rsstudio.taskify.component.dropdown.DropDown
import com.rsstudio.taskify.component.dropdown.MenuActionItem
import com.rsstudio.taskify.component.textfield.OutlinedTextFieldWithLabel
import com.rsstudio.taskify.jobs.ReminderWorker
import com.rsstudio.taskify.ui.navigation.actions.AddOrEditReminderScreenActions
import com.rsstudio.taskify.ui.theme.Purple40
import com.rsstudio.taskify.ui.theme.ht2
import com.rsstudio.taskify.ui.theme.label
import com.rsstudio.taskify.ui.theme.lightBlack
import com.rsstudio.taskify.ui.theme.white
import com.rsstudio.taskify.util.TimeUtil


const val menuItemId = "1"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditReminderScreen(
    viewModel: AddOrEditReminderViewModel = hiltViewModel(),
    onAction: (addOrEditReminderScreenActions: AddOrEditReminderScreenActions) -> Unit
) {
    var openMenu by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (viewModel.uiState.editingReminder) {
                            stringResource(AppString.edit_reminder)
                        } else stringResource(
                            AppString.add_reminder
                        ),
                        style = MaterialTheme.typography.ht2.copy(
                            fontSize = 14.sp,
                            color = lightBlack
                        )
                    )
                },
                navigationIcon = {
                    Row {
                        Spacer(modifier = Modifier.size(16.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back",
                            modifier = Modifier.noRippleClickable {
                                onAction(AddOrEditReminderScreenActions.OnBack)
                            }
                        )
                    }
                },
                actions = {
                    if (viewModel.uiState.editingReminder) {
                        Icon(
                            modifier = Modifier.noRippleClickable {
                                openMenu = true
                            },
                            painter = painterResource(id = AppDrawable.ic_menu),
                            contentDescription = ""
                        )
                        DropDown(
                            openMenu = openMenu,
                            menuItems = listOf(
                                MenuActionItem(
                                    menuItemId,
                                    title = stringResource(id = AppString.delete)
                                )
                            ),
                            onDismissMenu = {
                                openMenu = false
                            },
                            onTitleBarActionClick = {
                                if (it.id == menuItemId) {
                                    viewModel.onEvent(AddOrEditReminderUIEvent.OnDeleteReminder)
                                }
                                openMenu = false
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                    .copy(containerColor = white),
            )
        }
    ) {
        AddOrEditReminderContent(
            modifier = Modifier.padding(it),
            uiState = viewModel.uiState,
            onEvent = viewModel::onEvent
        )
    }

    LaunchedEffect(key1 = viewModel.uiSideEffect) {
        handelSideEffects(
            context = context,
            uiState = viewModel.uiState,
            sideEffects = viewModel.uiSideEffect,
            onAction = onAction
        )
        viewModel.resetUiSideEffect()
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddOrEditReminderContent(
    modifier: Modifier = Modifier,
    uiState: AddOrEditReminderUiState,
    onEvent: (AddOrEditReminderUIEvent) -> Unit
) {
    val context = LocalContext.current
    val permissionGranted = remember { mutableStateOf(false) }
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted.value = isGranted
        if (!isGranted) {
            val shouldShowRationale =
                androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as androidx.activity.ComponentActivity),
                    Manifest.permission.POST_NOTIFICATIONS
                )
            if (shouldShowRationale) {
                showRationaleDialog = true
            } else {
                showSettingsDialog = true
            }
        }
    }

    if (showRationaleDialog) {
        ShowRationaleDialog(
            context = context,
            permissionLauncher = permissionLauncher
        )
    }

    if (showSettingsDialog) {
        ShowSettingsDialog(context = context)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(white)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextFieldWithLabel(
            label = stringResource(AppString.add_title),
            value = uiState.titleTextFieldValue,
            onTextChange = {
                onEvent(AddOrEditReminderUIEvent.OnTitleTextChanged(it))
            }
        )
        OutlinedTextFieldWithLabel(
            label = stringResource(AppString.add_description),
            value = uiState.descriptionTextFieldValue,
            onTextChange = {
                onEvent(AddOrEditReminderUIEvent.OnDescriptionTextChanged(it))
            }
        )
        Text(
            text = stringResource(AppString.choose_your_reminder_interval),
            style = MaterialTheme.typography.titleMedium
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            stringArrayResource(id = AppArray.interval_info).toList().forEach {
                Chip(
                    selected = uiState.selectedReminderInterval == it,
                    text = it,
                    onClick = {
                        if (permissionGranted.value) {
                            onEvent(AddOrEditReminderUIEvent.OnSelectReminderInterval(it))
                        } else {
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple40
            ),
            enabled = uiState.validateFields(),
            onClick = {
                onEvent(AddOrEditReminderUIEvent.OnAddOrUpdateReminder)
            }
        ) {
            Text(
                text = if (uiState.editingReminder) stringResource(AppString.update) else stringResource(
                    AppString.add
                ),
                style = MaterialTheme.typography.label.copy(color = white)
            )
        }
    }
}

private fun handelSideEffects(
    context: Context,
    sideEffects: AddOrEditReminderSideEffects,
    uiState: AddOrEditReminderUiState,
    onAction: (actions: AddOrEditReminderScreenActions) -> Unit,
) {
    when (sideEffects) {
        is AddOrEditReminderSideEffects.OnDone -> {
            if (sideEffects.delete) {
                ReminderWorker.cancelWork(context, uiState.reminderId)
            } else {
                Log.d("liom77", "handelSideEffects: ${uiState.getTime()} ")
                Log.d(
                    "liom77",
                    "handelSideEffects: ${TimeUtil.formatTimeInMillisTo12HourFormat(uiState.getTime())} "
                )
                ReminderWorker.scheduleWork(
                    scheduleWork = uiState.selectedReminderInterval.isNotEmpty(),
                    context = context,
                    interval = uiState.getIntervalUnit(),
                    intervalUnit = uiState.getTimeInterval(),
                    title = uiState.titleTextFieldValue.text,
                    description = uiState.descriptionTextFieldValue.text,
                    tag = uiState.reminderId,
                    time = TimeUtil.formatTimeInMillisTo12HourFormat(uiState.getTime()),
                    fromApi = uiState.fromApi
                )
            }
            onAction(AddOrEditReminderScreenActions.OnBack)
        }

        else -> {}
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AddEditReminderScreenPreview() {
    var openMenu by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Reminder",
                        style = MaterialTheme.typography.ht2.copy(
                            fontSize = 14.sp,
                            color = lightBlack
                        )
                    )
                },
                navigationIcon = {
                    Row {
                        Spacer(modifier = Modifier.size(16.dp))
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                },
                actions = {
                    Icon(
                        modifier = Modifier.noRippleClickable {
                            openMenu = true
                        },
                        painter = painterResource(id = AppDrawable.ic_menu),
                        contentDescription = ""
                    )
                    DropDown(
                        openMenu = openMenu,
                        menuItems = listOf(
                            MenuActionItem(
                                id = menuItemId,
                                title = stringResource(id = AppString.delete)
                            )
                        ),
                        onDismissMenu = {
                            openMenu = false
                        },
                        onTitleBarActionClick = {
                            openMenu = false
                        }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                    .copy(containerColor = white),
            )
        }
    ) {
        AddOrEditReminderContent(
            modifier = Modifier.padding(it),
            uiState = AddOrEditReminderUiState(),
            onEvent = {}
        )
    }
}
