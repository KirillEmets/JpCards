package com.kirillemets.flashcards.domain.service

import com.kirillemets.flashcards.domain.model.ExportInfo
import com.kirillemets.flashcards.domain.model.Note

interface NoteExporterService {
    suspend fun exportNotes(notes: List<Note>, exportInfo: ExportInfo)
}