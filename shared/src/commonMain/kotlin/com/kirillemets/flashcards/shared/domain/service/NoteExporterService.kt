package com.kirillemets.flashcards.shared.domain.service

import com.kirillemets.flashcards.shared.domain.model.ExportInfo
import com.kirillemets.flashcards.shared.domain.model.Note

interface NoteExporterService {
    suspend fun exportNotes(notes: List<Note>, exportInfo: ExportInfo)
}