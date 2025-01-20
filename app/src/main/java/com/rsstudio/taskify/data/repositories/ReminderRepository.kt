package com.rsstudio.taskify.data.repositories

import com.rsstudio.taskify.data.local.entity.ReminderEntity
import com.rsstudio.taskify.data.sources.local.ReminderLocalDataSource
import com.rsstudio.taskify.data.sources.remote.ReminderRemoteDataSource
import com.rsstudio.taskify.domain.mapper.toData
import com.rsstudio.taskify.domain.mapper.toRoomEntity
import com.rsstudio.taskify.domain.mapper.toUiData
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val remoteDataSource: ReminderRemoteDataSource,
    private val localDataSource: ReminderLocalDataSource,
) {

    fun fetchRemoteReminders() = remoteDataSource.fetchReminder()

    fun fetchReminderListFromDb() = flow {
        val localData = localDataSource.getAllReminders().firstOrNull()
        val apiResponse = remoteDataSource.fetchReminder().firstOrNull()
        when {
            localData.isNullOrEmpty() -> {
                apiResponse?.takeIf { it.isSuccessful }?.body()?.let {
                    localDataSource.insertReminders(reminders = it.toRoomEntity())
                    emit(it.toData())
                }
            }

            else -> {
                emit(localData.toUiData())
            }
        }
    }

    fun getReminderById(id: String) = localDataSource.getReminderById(id)

    suspend fun insertSingleReminderToDB(reminderEntity: ReminderEntity) =
        localDataSource.insertReminder(reminderEntity)

    suspend fun insertRemindersToDB(beneficiaries: List<ReminderEntity>) =
        localDataSource.insertReminders(beneficiaries)

    suspend fun deleteReminderById(id: String) = localDataSource.deleteReminderById(id)

    suspend fun updateReminder(reminderEntity: ReminderEntity) =
        localDataSource.updateReminder(reminderEntity)

}
