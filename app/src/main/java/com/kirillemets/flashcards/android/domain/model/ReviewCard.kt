package com.kirillemets.flashcards.android.domain.model

data class ReviewCard(
    val noteId: Int,
    val word: String,
    val wordReading: String,
    val answer: String,
    val answerReading: String,
    val reversed: Boolean,
    val lastDelay: Int
) {
    companion object {
        val Empty = ReviewCard(0, "", "", "", "", false, 0)
        fun fromNote(note: Note): ReviewCard = ReviewCard(
            note.noteId,
            note.japanese,
            note.reading,
            note.english,
            "",
            false,
            note.lastDelay
        )

        fun fromNoteReversed(note: Note): ReviewCard = ReviewCard(
            note.noteId,
            note.english,
            "",
            note.japanese,
            note.reading,
            true,
            note.lastDelayReversed
        )
    }
}
