package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.model.Note
import com.kirillemets.flashcards.shared.domain.repository.NoteRepository

class AddNotesUseCase constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<Note>) {
        return noteRepository.insertNew(notes)
    }
}