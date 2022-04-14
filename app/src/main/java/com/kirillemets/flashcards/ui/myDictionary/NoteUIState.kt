package com.kirillemets.flashcards.ui.myDictionary

import com.kirillemets.flashcards.domain.model.Note

data class NoteUIState(
    val noteId: Int,
    val japanese: String,
    val reading: String,
    val english: String,
    val lastDelay: Int,
    val lastDelayReversed: Int,
) {
    companion object {
        fun fromNote(note: Note): NoteUIState = NoteUIState(
            note.noteId,
            note.japanese,
            note.reading,
            note.english,
            note.lastDelay,
            note.lastDelayReversed
        )
    }
}