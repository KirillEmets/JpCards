package com.kirillemets.flashcards.data.model

import com.kirillemets.flashcards.domain.model.Note

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

fun Note.toFlashCard(): FlashCard = FlashCard(
    0,
    japanese,
    reading,
    english,
    lastDelay,
    nextReviewTime,
    lastDelayReversed,
    nextReviewTimeReversed
)

fun List<Note>.toFlashCards() = this.map { it.toFlashCard() }