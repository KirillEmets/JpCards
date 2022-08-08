package com.kirillemets.flashcards.android.domain.usecase

import com.kirillemets.flashcards.android.domain.repository.NoteRepository
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