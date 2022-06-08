package com.kirillemets.flashcards.data.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.kirillemets.flashcards.data.AppPreferencesImpl
import com.kirillemets.flashcards.data.repository.NoteRepositoryImpl
import com.kirillemets.flashcards.domain.AppPreferences
import com.kirillemets.flashcards.domain.repository.NoteRepository
import com.kirillemets.flashcards.data.database.CardDatabase
import com.kirillemets.flashcards.data.database.CardDatabaseDao
import com.kirillemets.flashcards.data.apiService.JishoApiService
import com.kirillemets.flashcards.data.repository.DictionaryRepositoryImpl
import com.kirillemets.flashcards.data.service.NoteExporterServiceImpl
import com.kirillemets.flashcards.domain.repository.DictionaryRepository
import com.kirillemets.flashcards.domain.service.NoteExporterService
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
object NetworkModule {
    private const val BASE_URL = "https://jisho.org/api/v1/search/words/"

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
