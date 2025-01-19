package com.rsstudio.taskify.data.restclient

import com.rsstudio.taskify.data.models.response.ReminderResponse
import retrofit2.Response
import retrofit2.http.GET

interface AppApiClientService {
    @GET(".")
    suspend fun fetchReminders(): Response<List<ReminderResponse>>

    companion object {
        private const val PATH_EXAMPLE_DATA = "top-headlines"
    }
}