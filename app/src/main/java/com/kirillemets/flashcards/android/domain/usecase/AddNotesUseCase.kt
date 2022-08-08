package com.kirillemets.flashcards.android.domain.usecase

import com.kirillemets.flashcards.android.domain.model.Note
import com.kirillemets.flashcards.android.domain.repository.NoteRepository
import javax.inject.Inject

class AddNotesUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<Note>) {
        return noteRepository.insertNew(notes)
    }
}