package com.kirillemets.flashcards.android.data.di

import com.kirillemets.flashcards.android.data.repository.NoteRepositoryImpl
import com.kirillemets.flashcards.android.domain.repository.NoteRepository
import com.kirillemets.flashcards.android.data.database.CardDatabaseDao
import com.kirillemets.flashcards.android.data.apiService.JishoApiService
import com.kirillemets.flashcards.android.data.repository.DictionaryRepositoryImpl
import com.kirillemets.flashcards.android.domain.repository.DictionaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideNoteRepository(dao: CardDatabaseDao): NoteRepository {
        return NoteRepositoryImpl(dao)
    }

    @Provides
    fun provideDictionaryRepository(jishoApiService: JishoApiService): DictionaryRepository {
        return DictionaryRepositoryImpl(jishoApiService)
    }
}
