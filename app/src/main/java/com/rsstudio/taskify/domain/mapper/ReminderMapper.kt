package com.rsstudio.taskify.domain.mapper

import com.rsstudio.taskify.data.local.entity.ReminderEntity
import com.rsstudio.taskify.data.models.response.ReminderResponse
import com.rsstudio.taskify.ui.screen.addoreditreminder.AddOrEditReminderViewModel.Companion.DAILY
import com.rsstudio.taskify.ui.screen.addoreditreminder.AddOrEditReminderViewModel.Companion.EVERY_15_MINUTES
import com.rsstudio.taskify.ui.screen.addoreditreminder.AddOrEditReminderViewModel.Companion.HOURLY
import com.rsstudio.taskify.ui.screen.reminderlist.Reminder
import com.rsstudio.taskify.util.TimeUtil

fun List<ReminderResponse>.toData() = this.map { response ->
    Reminder(
        id = response.id.toString(),
        title = response.title,
        description = "",
        interval = "",
        fromApi = true,
    )
}


fun List<ReminderEntity>.toUiData() = this.map {
    Reminder(
        id = it.id,
        title = it.title,
        description = it.description ?: "",
        interval = it.reminderInterval,
        fromApi = it.fromApi
    )
}

fun List<ReminderResponse>.toRoomEntity() = this.map {
    ReminderEntity(
        id = it.id.toString(),
        title = it.title,
        description = "",
        reminderInterval = "",
        timeStamp = TimeUtil.getTimeStamp(),
        fromApi = true
    )
}
