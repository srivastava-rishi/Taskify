package com.rsstudio.taskify.domain.db

import com.rsstudio.taskify.data.repositories.ReminderRepository
import javax.inject.Inject

class DeleteReminderByIdToDBUseCase @Inject constructor(private val repository: ReminderRepository) {

    suspend operator fun invoke(id: String) = repository.deleteReminderById(id)
}