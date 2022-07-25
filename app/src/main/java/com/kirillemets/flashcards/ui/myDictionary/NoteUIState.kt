package com.kirillemets.flashcards.ui.myDictionary

import com.kirillemets.flashcards.domain.model.Note

data class NoteUIState(
    val noteId: Int = -1,
    val japanese: String = "",
    val reading: String = "",
    val english: String = "",
    val lastDelay: Int = -1,
    val lastDelayReversed: Int = -1,
    val selected: Boolean = false,
    val opened: Boolean = false
) {
    companion object {
        fun fromNote(note: Note, selected: Boolean, opened: Boolean): NoteUIState = NoteUIState(
            note.noteId,
            note.japanese,
            note.reading,
            note.english,
            note.lastDelay,
            note.lastDelayReversed,
            selected,
            opened
        )
    }
}