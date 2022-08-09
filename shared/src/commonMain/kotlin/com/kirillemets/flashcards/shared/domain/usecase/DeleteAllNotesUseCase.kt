package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.repository.NoteRepository

class DeleteAllNotesUseCase constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke() {
        return noteRepository.deleteAll()
    }
}