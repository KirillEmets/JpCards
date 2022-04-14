package com.kirillemets.flashcards.domain.usecase

import com.kirillemets.flashcards.domain.model.ExportInfo
import com.kirillemets.flashcards.domain.repository.NoteRepository
import com.kirillemets.flashcards.domain.service.NoteExporterService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExportNotesUseCase @Inject constructor(
    private val noteExporterService: NoteExporterService,
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(exportInfo: ExportInfo) {
        val notes = noteRepository.getAll().first()
        noteExporterService.exportNotes(notes, exportInfo)
    }
}