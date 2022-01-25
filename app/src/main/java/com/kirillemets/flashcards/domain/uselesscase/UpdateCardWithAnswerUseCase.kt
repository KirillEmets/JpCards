package com.kirillemets.flashcards.domain.uselesscase

import com.kirillemets.flashcards.domain.model.AnswerType
import com.kirillemets.flashcards.domain.repository.NoteRepository
import com.kirillemets.flashcards.domain.model.ReviewCard
import org.joda.time.DateTime
import javax.inject.Inject

class UpdateCardWithAnswerUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val getNewDelayInDaysUseCase: GetNewDelayInDaysUseCase
) {
    suspend operator fun invoke(
        reviewCard: ReviewCard,
        answerType: AnswerType,
        todayTimeMillis: Long
    ) {
        var newDelay = getNewDelayInDaysUseCase(reviewCard.lastDelay, answerType)
        val nextRepeatMillis = DateTime(todayTimeMillis).plusDays(newDelay).millis

        if (newDelay == 0) newDelay = 1

        val update =
            if (reviewCard.reversed)
                noteRepository::updateReversedDelayAndTime
            else
                noteRepository::updateRegularDelayAndTime

        update(reviewCard.cardId, newDelay, nextRepeatMillis)
    }
}