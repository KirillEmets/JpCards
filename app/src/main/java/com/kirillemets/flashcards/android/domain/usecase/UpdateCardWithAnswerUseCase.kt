package com.kirillemets.flashcards.android.domain.usecase

import com.kirillemets.flashcards.android.domain.model.AnswerType
import com.kirillemets.flashcards.android.domain.repository.NoteRepository
import com.kirillemets.flashcards.android.domain.model.ReviewCard
import org.joda.time.DateTime
import javax.inject.Inject

class UpdateCardWithAnswerUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val getNewDelayInDaysUseCase: GetNewDelayInDaysUseCase,
) {
    suspend operator fun invoke(
        reviewCard: ReviewCard,
        answerType: AnswerType,
        todayTimeMillis: Long
    ) {
        val nextDelay = getNewDelayInDaysUseCase(reviewCard.lastDelay, answerType)
        val nextReviewTime = DateTime(todayTimeMillis).plusDays(nextDelay).millis

        val update =
            if (reviewCard.reversed)
                noteRepository::updateReversedDelayAndTime
            else
                noteRepository::updateRegularDelayAndTime

        update(reviewCard.noteId, nextDelay, nextReviewTime)
    }
}