package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.model.Note
import com.kirillemets.flashcards.shared.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetAllCardsUseCase constructor(private val noteRepository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return noteRepository.getAll()
    }
}