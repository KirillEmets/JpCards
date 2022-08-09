package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.model.ExportInfo
import com.kirillemets.flashcards.shared.domain.repository.NoteRepository
import com.kirillemets.flashcards.shared.domain.service.NoteExporterService
import kotlinx.coroutines.flow.first

class ExportNotesUseCase constructor(
    private val noteExporterService: NoteExporterService,
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(exportInfo: ExportInfo) {
        val notes = noteRepository.getAll().first()
        noteExporterService.exportNotes(notes, exportInfo)
    }
}