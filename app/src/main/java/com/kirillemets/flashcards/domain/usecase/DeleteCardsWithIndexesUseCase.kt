package com.kirillemets.flashcards.domain.usecase

import com.kirillemets.flashcards.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteCardsWithIndexesUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(ids: Iterable<Int>) {
        noteRepository.deleteByIndexes(ids.toSet())
    }

    suspend operator fun invoke(vararg ids: Int) {
        noteRepository.deleteByIndexes(ids.toSet())
    }
}