package com.kirillemets.flashcards.domain.usecase

import com.kirillemets.flashcards.domain.model.Note
import com.kirillemets.flashcards.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddNotesUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<Note>) {
        return noteRepository.insertNew(notes)
    }
}