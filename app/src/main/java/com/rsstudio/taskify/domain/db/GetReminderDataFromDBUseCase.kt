package com.rsstudio.taskify.domain.db

import com.rsstudio.taskify.data.repositories.ReminderRepository
import javax.inject.Inject


class GetReminderDataFromDBUseCase @Inject constructor(private val repository: ReminderRepository ) {

    operator fun invoke() = repository.fetchReminderListFromDb()
}