package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.repository.NoteRepository
import com.kirillemets.flashcards.shared.domain.model.ReviewCard
import com.kirillemets.flashcards.shared.util.TimeUtil

class LoadCardForReviewUseCase constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(currentTimeMillis: Long): List<ReviewCard> {
        noteRepository.getNotesWithRelevantCards(currentTimeMillis).let { notes ->
            val currentTime = TimeUtil.getCurrentTimeMillis()
            val relevantReviewCards = mutableListOf<ReviewCard>()
            notes.forEach { note ->
                if (note.nextReviewTime <= currentTime)
                    relevantReviewCards.add(
                        ReviewCard.fromNote(note)
                    )
                if (note.nextReviewTimeReversed <= currentTime)
                    relevantReviewCards.add(
                        ReviewCard.fromNoteReversed(note)
                    )
            }

            return relevantReviewCards
        }
    }
}