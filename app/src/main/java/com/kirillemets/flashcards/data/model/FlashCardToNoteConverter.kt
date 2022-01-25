package com.kirillemets.flashcards.data.model

import com.kirillemets.flashcards.domain.model.Note
import com.kirillemets.flashcards.model.FlashCard

fun FlashCard.toNote() = Note(
    cardId,
    japanese,
    reading,
    english,
    lastDelay,
    nextReviewTime,
    lastDelayReversed,
    nextReviewTimeReversed
)

fun List<FlashCard>.toNotes() = this.map { it.toNote() }