package com.kirillemets.flashcards.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.network.JishoApiService
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
object FlashcardModule {
    private const val BASE_URL = "https://jisho.org/api/v1/search/words/"

    @Singleton
    @Provides
    fun provideCardDatabase(@ApplicationContext app: Context): CardDatabase {
        return CardDatabase.getInstance(app)
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
