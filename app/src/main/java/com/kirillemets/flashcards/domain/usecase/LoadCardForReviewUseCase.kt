package com.kirillemets.flashcards.domain.usecase

import com.kirillemets.flashcards.domain.repository.NoteRepository
import com.kirillemets.flashcards.domain.model.ReviewCard
import org.joda.time.LocalDate
import javax.inject.Inject

class LoadCardForReviewUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(currentTimeMillis: Long): List<ReviewCard> {
        noteRepository.getNotesWithRelevantCards(currentTimeMillis).let { notes ->
            val currentTime = LocalDate.now().toDateTimeAtCurrentTime().millis
            val relevantReviewCards = mutableListOf<ReviewCard>()
            notes.forEach { note ->
                if (note.nextReviewTime <= currentTime)
                    relevantReviewCards.add(
                        ReviewCard.fromNote(note, currentTime)
                    )
                if (note.nextReviewTimeReversed <= currentTime)
                    relevantReviewCards.add(
                        ReviewCard.fromNoteReversed(note, currentTime)
                    )
            }

            return relevantReviewCards
        }
    }
}