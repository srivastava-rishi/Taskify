package com.rsstudio.taskify.domain.db

import com.rsstudio.taskify.data.local.entity.ReminderEntity
import com.rsstudio.taskify.data.repositories.ReminderRepository
import java.util.UUID
import javax.inject.Inject

class AddReminderToDBUseCase @Inject constructor(private val repository: ReminderRepository) {
    suspend operator fun invoke(
        title: String,
        description: String,
        reminderInterval: String
    ) = repository.insertSingleReminderToDB(
        ReminderEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            timeStamp = System.currentTimeMillis(),
            reminderInterval = reminderInterval
        )
    )
}