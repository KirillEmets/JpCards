package com.kirillemets.flashcards.android.data.di

import android.content.Context
import com.kirillemets.flashcards.android.data.AppPreferencesImpl
import com.kirillemets.flashcards.shared.domain.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppPreferencesModule {

    @Provides
    fun provideAppPreferences(@ApplicationContext app: Context): AppPreferences {
        return AppPreferencesImpl(app)
    }
}
