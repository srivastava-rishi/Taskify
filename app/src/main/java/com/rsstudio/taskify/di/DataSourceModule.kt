package com.rsstudio.taskify.di


import com.rsstudio.taskify.data.local.dao.ReminderDao
import com.rsstudio.taskify.data.restclient.AppApiClientService
import com.rsstudio.taskify.data.sources.local.ReminderLocalDataSource
import com.rsstudio.taskify.data.sources.remote.ReminderRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun reminderLocalDataSource(
        dao: ReminderDao
    ) = ReminderLocalDataSource(dao)

    @Provides
    fun reminderRemoteDataSource(service: AppApiClientService) = ReminderRemoteDataSource(service)
}