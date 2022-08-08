package com.kirillemets.flashcards.android.data.di

import android.content.Context
import androidx.room.Room
import com.kirillemets.flashcards.android.data.database.CardDatabase
import com.kirillemets.flashcards.android.data.database.CardDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideCardDatabase(@ApplicationContext app: Context): CardDatabase {
        return Room.databaseBuilder(
            app,
            CardDatabase::class.java,
            "flashcards_database"
        ).build()
    }

    @Provides
    fun provideCardDatabaseDao(cardDatabase: CardDatabase): CardDatabaseDao {
        return cardDatabase.flashCardsDao()
    }
}
