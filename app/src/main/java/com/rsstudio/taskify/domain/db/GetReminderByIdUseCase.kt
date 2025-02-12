package com.rsstudio.taskify.domain.db

import com.rsstudio.taskify.data.repositories.ReminderRepository
import javax.inject.Inject

class GetReminderByIdUseCase @Inject constructor(private val repository: ReminderRepository) {

    suspend operator fun invoke(id:String) = repository.getReminderById(id)
}