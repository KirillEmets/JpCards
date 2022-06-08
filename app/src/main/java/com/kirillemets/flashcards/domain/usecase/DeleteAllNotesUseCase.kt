package com.kirillemets.flashcards.domain.usecase

import com.kirillemets.flashcards.domain.model.Note
import com.kirillemets.flashcards.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAllNotesUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke() {
        return noteRepository.deleteAll()
    }
}