package com.rsstudio.taskify.di

import android.content.Context
import com.rsstudio.taskify.domain.local.TextToSpeechUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun getTextToSpeechUseCase(
        @ApplicationContext context: Context
    ) = TextToSpeechUseCase(context)
}