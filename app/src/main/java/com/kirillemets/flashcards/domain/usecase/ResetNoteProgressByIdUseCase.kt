package com.kirillemets.flashcards.domain.usecase

import com.kirillemets.flashcards.domain.repository.NoteRepository
import javax.inject.Inject

class ResetNoteProgressByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(
        noteIds: Set<Int>,
    ) {
        noteRepository.resetDelayByIds(noteIds)
        noteRepository.resetDelayByIdsReversed(noteIds)
    }
}