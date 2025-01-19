package com.rsstudio.taskify.data.sources.remote

import com.rsstudio.taskify.data.restclient.AppApiClientService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReminderRemoteDataSource @Inject constructor(
    private val service: AppApiClientService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun fetchReminder() = flow {
        emit(service.fetchReminders())
    }.flowOn(dispatcher)
}