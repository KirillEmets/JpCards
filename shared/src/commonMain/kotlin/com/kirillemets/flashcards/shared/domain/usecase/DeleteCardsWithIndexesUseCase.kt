package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.repository.NoteRepository

class DeleteCardsWithIndexesUseCase constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(ids: Iterable<Int>) {
        noteRepository.deleteByIndexes(ids.toSet())
    }

    suspend operator fun invoke(vararg ids: Int) {
        noteRepository.deleteByIndexes(ids.toSet())
    }
}