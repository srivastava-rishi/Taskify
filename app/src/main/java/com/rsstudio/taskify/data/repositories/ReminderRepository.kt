package com.rsstudio.taskify.data.repositories

import com.rsstudio.taskify.data.local.entity.ReminderEntity
import com.rsstudio.taskify.data.sources.local.ReminderLocalDataSource
import com.rsstudio.taskify.data.sources.remote.ReminderRemoteDataSource
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val remoteDataSource: ReminderRemoteDataSource,
    private val localDataSource: ReminderLocalDataSource,
) {

    fun fetchRemoteReminders() = remoteDataSource.fetchReminder()

    fun fetchReminderListFromDb() = localDataSource.getAllReminders()

    fun getReminderById(id: String) = localDataSource.getReminderById(id)

    suspend fun insertSingleReminderToDB(reminderEntity: ReminderEntity) =
        localDataSource.insertReminder(reminderEntity)

    suspend fun insertRemindersToDB(beneficiaries: List<ReminderEntity>) =
        localDataSource.insertReminders(beneficiaries)

    suspend fun deleteReminderById(id: String) = localDataSource.deleteReminderById(id)

    suspend fun updateReminder(reminderEntity: ReminderEntity) =
        localDataSource.updateReminder(reminderEntity)

}
