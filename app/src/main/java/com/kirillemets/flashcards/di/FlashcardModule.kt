package com.kirillemets.flashcards.di

import android.content.Context
import com.kirillemets.flashcards.database.CardDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlashcardModule {
    @Singleton
    @Provides
    fun provideCardDatabase(@ApplicationContext app: Context): CardDatabase {
        return CardDatabase.getInstance(app)
    }
}
