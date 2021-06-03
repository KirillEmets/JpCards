package com.kirillemets.flashcards.review

import com.kirillemets.flashcards.database.FlashCard

data class ReviewCard(
    val word: String,
    val wordReading: String,
    val answer: String,
    val answerReading: String,
    val reversed: Boolean,
    val lastDelay: Int,
    val cardId: Int) {
    companion object {
        fun fromDataBaseFlashCardDefault(card: FlashCard): ReviewCard = ReviewCard(
            card.japanese,
            card.reading,
            card.english,
            "",
            false,
            card.lastDelay,
            card.cardId
        )
        fun fromDataBaseFlashCardReversed(card: FlashCard): ReviewCard = ReviewCard(
            card.english,
            "",
            card.japanese,
            card.reading,
            true,
            card.lastDelayReversed,
            card.cardId
        )
    }
}