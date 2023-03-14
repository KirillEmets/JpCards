package com.kirillemets.flashcards.domain.model

data class ReviewCard(
    val noteId: Int,
    val word: String,
    val wordReading: String,
    val answer: String,
    val answerReading: String,
    val reversed: Boolean,
    val lastDelay: Int,
    val overdue: Long
) {
    companion object {
        val Empty = ReviewCard(0, "", "", "", "", false, 0, 0)
        fun fromNote(note: Note, currentTimeMillis: Long): ReviewCard = ReviewCard(
            note.noteId,
            note.japanese,
            note.reading,
            note.english,
            "",
            false,
            note.lastDelay,
            currentTimeMillis - note.nextReviewTime
        )

        fun fromNoteReversed(note: Note, currentTimeMillis: Long): ReviewCard = ReviewCard(
            note.noteId,
            note.english,
            "",
            note.japanese,
            note.reading,
            true,
            note.lastDelayReversed,
            currentTimeMillis - note.nextReviewTimeReversed
        )
    }
}
