package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.repository.NoteRepository

class ResetNoteProgressByIdUseCase constructor(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(
        noteIds: Set<Int>,
    ) {
        noteRepository.resetDelayByIds(noteIds)
        noteRepository.resetDelayByIdsReversed(noteIds)
    }
}