package com.kirillemets.flashcards.domain.uselesscase

import com.kirillemets.flashcards.domain.AppPreferences
import com.kirillemets.flashcards.domain.model.AnswerType
import com.kirillemets.flashcards.domain.repository.NoteRepository
import javax.inject.Inject
import kotlin.math.roundToInt

class DeleteCardsWithIndexesUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(ids: Iterable<Int>) {
        noteRepository.deleteByIndexes(ids.toSet())
    }

    suspend operator fun invoke(vararg ids: Int) {
        noteRepository.deleteByIndexes(ids.toSet())
    }
}