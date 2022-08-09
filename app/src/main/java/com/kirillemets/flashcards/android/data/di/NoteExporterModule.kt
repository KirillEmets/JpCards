package com.kirillemets.flashcards.android.data.di

import android.content.Context
import com.kirillemets.flashcards.android.data.service.NoteExporterServiceImpl
import com.kirillemets.flashcards.shared.domain.service.NoteExporterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NoteExporterModule {

    @Provides
    fun provideNoteExporterService(@ApplicationContext app: Context) : NoteExporterService {
        return NoteExporterServiceImpl(app)
    }
}
