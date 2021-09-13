package com.kirillemets.flashcards

import android.content.Context
import androidx.room.Room
import com.kirillemets.flashcards.model.database.CardDatabase
import com.kirillemets.flashcards.model.database.CardDatabaseDao
import com.kirillemets.flashcards.di.FlashcardRepositoryModule
import com.kirillemets.flashcards.model.network.JishoApiService
import com.kirillemets.flashcards.model.network.QueryData
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [FlashcardRepositoryModule::class])
object TestFlashcardRepositoryModule {
    @Singleton
    @Provides
    fun provideCardDatabase(@ApplicationContext app: Context): CardDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            CardDatabase::class.java,
        ).build()
    }

    @Provides
    fun provideCardDatabaseDao(cardDatabase: CardDatabase): CardDatabaseDao {
        return cardDatabase.flashCardsDao()
    }

    @Singleton
    @Provides
    fun provideJishoApiService(): JishoApiService {
        return object : JishoApiService {
            override fun getDataObjectAsync(word: String): Deferred<QueryData> {
                val c = CompletableDeferred<QueryData>()
                c.completeExceptionally(Exception("STAB"))
                return c
            }

        }
    }
}