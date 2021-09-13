package com.kirillemets.flashcards.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kirillemets.flashcards.model.database.CardDatabase
import com.kirillemets.flashcards.model.database.CardDatabaseDao
import com.kirillemets.flashcards.model.network.JishoApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlashcardRepositoryModule {
    private const val BASE_URL = "https://jisho.org/api/v1/search/words/"

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

    @Singleton
    @Provides
    fun provideJishoApiService(): JishoApiService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(JishoApiService::class.java)
    }
}
