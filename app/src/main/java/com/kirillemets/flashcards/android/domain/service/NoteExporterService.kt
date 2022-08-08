package com.kirillemets.flashcards.android.domain.service

import com.kirillemets.flashcards.android.domain.model.ExportInfo
import com.kirillemets.flashcards.android.domain.model.Note

interface NoteExporterService {
    suspend fun exportNotes(notes: List<Note>, exportInfo: ExportInfo)
}