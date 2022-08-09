package com.kirillemets.flashcards.android.ui.review

import com.kirillemets.flashcards.shared.domain.model.ReviewCard

data class ReviewCardUIState(
    val word: String = "",
    val wordReading: String = "",
    val answer: String = "",
    val answerReading: String = "",
    val lastDelay: Int = 0
) {
    companion object {
        fun fromReviewCard(reviewCard: ReviewCard): ReviewCardUIState = ReviewCardUIState(
            reviewCard.word,
            reviewCard.wordReading,
            reviewCard.answer,
            reviewCard.answerReading,
            reviewCard.lastDelay
        )
    }
}
