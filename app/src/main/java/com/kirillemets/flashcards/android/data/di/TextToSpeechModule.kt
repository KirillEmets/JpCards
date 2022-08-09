package com.kirillemets.flashcards.android.data.di

import android.content.Context
import com.kirillemets.flashcards.android.data.service.TextToSpeechServiceImpl
import com.kirillemets.flashcards.shared.domain.service.TextToSpeechService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TextToSpeechModule {

    @Singleton
    @Provides
    fun provideTextToSpeechService(@ApplicationContext app: Context): TextToSpeechService {
        return TextToSpeechServiceImpl(app)
    }
}
