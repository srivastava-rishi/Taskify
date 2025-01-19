package com.rsstudio.taskify.di

import com.rsstudio.taskify.data.repositories.ReminderRepository
import com.rsstudio.taskify.data.sources.local.ReminderLocalDataSource
import com.rsstudio.taskify.data.sources.remote.ReminderRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun reminderRepository(
        remoteDataSource: ReminderRemoteDataSource,
        localDBDataSource: ReminderLocalDataSource
    ) = ReminderRepository(remoteDataSource, localDBDataSource)

}