package com.kirillemets.flashcards.android.data.di

import com.kirillemets.flashcards.android.data.apiService.JishoApiService
import com.kirillemets.flashcards.shared.domain.AppPreferences
import com.kirillemets.flashcards.shared.domain.repository.DictionaryRepository
import com.kirillemets.flashcards.shared.domain.repository.NoteRepository
import com.kirillemets.flashcards.shared.domain.service.NoteExporterService
import com.kirillemets.flashcards.shared.domain.service.TextToSpeechService
import com.kirillemets.flashcards.shared.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideAddNotesUseCase(noteRepository: NoteRepository): AddNotesUseCase {
        return AddNotesUseCase(noteRepository)
    }

    @Provides
    fun provideDeleteAllNotesUseCase(noteRepository: NoteRepository): DeleteAllNotesUseCase {
        return DeleteAllNotesUseCase(noteRepository)
    }

    @Provides
    fun provideDeleteCardsWithIndexesUseCase(noteRepository: NoteRepository): DeleteCardsWithIndexesUseCase {
        return DeleteCardsWithIndexesUseCase(noteRepository)
    }

    @Provides
    fun provideExportNotesUseCase(
        noteExporterService: NoteExporterService,
        noteRepository: NoteRepository
    ): ExportNotesUseCase {
        return ExportNotesUseCase(noteExporterService, noteRepository)
    }

    @Provides
    fun provideDFindWordsInDictionaryUseCase(dictionaryRepository: DictionaryRepository): FindWordsInDictionaryUseCase {
        return FindWordsInDictionaryUseCase(dictionaryRepository)
    }

    @Provides
    fun provideGetAllCardsUseCase(noteRepository: NoteRepository): GetAllCardsUseCase {
        return GetAllCardsUseCase(noteRepository)
    }

    @Provides
    fun provideGetNewDelayInDaysUseCase(appPreferences: AppPreferences): GetNewDelayInDaysUseCase {
        return GetNewDelayInDaysUseCase(appPreferences)
    }

    @Provides
    fun provideLoadCardForReviewUseCase(noteRepository: NoteRepository): LoadCardForReviewUseCase {
        return LoadCardForReviewUseCase(noteRepository)
    }

    @Provides
    fun provideResetNoteProgressByIdUseCase(noteRepository: NoteRepository): ResetNoteProgressByIdUseCase {
        return ResetNoteProgressByIdUseCase(noteRepository)
    }

    @Provides
    fun provideSpeakTextUseCase(textToSpeechService: TextToSpeechService): SpeakTextUseCase {
        return SpeakTextUseCase(textToSpeechService)
    }

    @Provides
    fun provideUpdateCardWithAnswerUseCase(
        noteRepository: NoteRepository,
        getNewDelayInDaysUseCase: GetNewDelayInDaysUseCase
    ): UpdateCardWithAnswerUseCase {
        return UpdateCardWithAnswerUseCase(noteRepository, getNewDelayInDaysUseCase)
    }
}
