package com.kirillemets.flashcards.domain.model

data class ReviewCard(
    val word: String,
    val wordReading: String,
    val answer: String,
    val answerReading: String,
    val reversed: Boolean,
    val lastDelay: Int,
    val cardId: Int
) {
    companion object {
        fun fromNote(note: Note): ReviewCard = ReviewCard(
            note.japanese,
            note.reading,
            note.english,
            "",
            false,
            note.lastDelay,
            note.cardId
        )

        fun fromNoteReversed(note: Note): ReviewCard = ReviewCard(
            note.english,
            "",
            note.japanese,
            note.reading,
            true,
            note.lastDelayReversed,
            note.cardId
        )
    }
}
