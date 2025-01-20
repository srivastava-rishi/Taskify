package com.rsstudio.taskify.domain.mapper

import com.rsstudio.taskify.data.local.entity.ReminderEntity
import com.rsstudio.taskify.data.models.response.ReminderResponse
import com.rsstudio.taskify.ui.screen.reminderlist.Reminder

fun List<ReminderResponse>.toData() = this.map { response ->
    Reminder(
        id = response.id.toString(),
        title = response.title,
        description = "",
        interval = ""
    )
}


fun List<ReminderEntity>.toUiData() = this.map {
    Reminder(
        id = it.id,
        title = it.title,
        description = it.description,
        interval = it.reminderInterval
    )
}
