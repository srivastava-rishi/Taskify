package com.rsstudio.taskify.data.sources.local

import com.rsstudio.taskify.data.local.dao.ReminderDao
import com.rsstudio.taskify.data.local.entity.ReminderEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReminderLocalDataSource @Inject constructor(
    private val dao: ReminderDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun insertReminder(reminder: ReminderEntity) {
        withContext(dispatcher) { dao.insertReminder(reminder) }
    }

    suspend fun insertReminders(reminders: List<ReminderEntity>) {
        withContext(dispatcher) { dao.insertAllReminders(reminders) }
    }

    suspend fun updateReminder(reminder: ReminderEntity) {
        withContext(dispatcher) {
            dao.updateReminder(reminder)
        }
    }

    suspend fun deleteReminderById(id: String) {
        withContext(dispatcher) {
            dao.deleteReminderById(id)
        }
    }

    fun getAllReminders(): Flow<List<ReminderEntity>> = dao.getAllReminders().flowOn(dispatcher)

    fun getReminderById(reminderId: String): Flow<ReminderEntity> =
        dao.getReminderById(reminderId).flowOn(dispatcher)

}
