package com.rsstudio.taskify.domain.db

import com.rsstudio.taskify.data.local.entity.ReminderEntity
import com.rsstudio.taskify.data.repositories.ReminderRepository
import javax.inject.Inject

class UpdateReminderDBUseCase @Inject constructor(
    private val repository: ReminderRepository
) {
    suspend operator fun invoke(
        id: String,
        title: String,
        description: String,
        reminderInterval: String
    ) = repository.updateReminder(
        ReminderEntity(
            id = id,
            title = title,
            description = description,
            timeStamp = System.currentTimeMillis(),
            reminderInterval = reminderInterval
        )
    )
}