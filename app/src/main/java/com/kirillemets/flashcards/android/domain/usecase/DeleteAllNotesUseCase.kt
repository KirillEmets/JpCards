package com.kirillemets.flashcards.android.domain.usecase

import com.kirillemets.flashcards.android.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteAllNotesUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke() {
        return noteRepository.deleteAll()
    }
}