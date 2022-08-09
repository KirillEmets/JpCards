package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.model.AnswerType
import com.kirillemets.flashcards.shared.domain.repository.NoteRepository
import com.kirillemets.flashcards.shared.domain.model.ReviewCard

class UpdateCardWithAnswerUseCase constructor(
    private val noteRepository: NoteRepository,
    private val getNewDelayInDaysUseCase: GetNewDelayInDaysUseCase,
) {
    suspend operator fun invoke(
        reviewCard: ReviewCard,
        answerType: AnswerType,
        todayTimeMillis: Long
    ) {
        val nextDelay = getNewDelayInDaysUseCase(reviewCard.lastDelay, answerType)
        val nextReviewTime = todayTimeMillis + 86400 * nextDelay

        val update =
            if (reviewCard.reversed)
                noteRepository::updateReversedDelayAndTime
            else
                noteRepository::updateRegularDelayAndTime

        update(reviewCard.noteId, nextDelay, nextReviewTime)
    }
}