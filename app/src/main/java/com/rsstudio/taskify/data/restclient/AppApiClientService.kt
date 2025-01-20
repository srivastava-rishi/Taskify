package com.rsstudio.taskify.data.restclient

import com.rsstudio.taskify.data.models.response.ReminderResponse
import retrofit2.Response
import retrofit2.http.GET

interface AppApiClientService {
    @GET(PATH_FETCH_REMINDERS)
    suspend fun fetchReminders(): Response<List<ReminderResponse>>

    companion object {
        private const val PATH_FETCH_REMINDERS = "todos"
    }
}