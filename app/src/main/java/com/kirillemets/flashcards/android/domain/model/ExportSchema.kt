package com.kirillemets.flashcards.android.domain.model

abstract class ExportSchema {
    fun noteToString(note: Note, withProgress: Boolean) =
        if (withProgress)
            noteToStringFull(note)
        else
            noteToStringSimplified(note)


    abstract val fileExtension: String

    protected abstract fun noteToStringFull(note: Note): String
    protected abstract fun noteToStringSimplified(note: Note): String

    object CSV : ExportSchema() {
        override val fileExtension: String
            get() = "csv"

        override fun noteToStringFull(note: Note): String =
            "\"${note.english}\"," +
                    "\"${note.japanese}\"," +
                    "\"${note.reading}\"," +
                    "\"${note.lastDelay}\"," +
                    "\"${note.lastDelayReversed}\"," +
                    "\"${note.nextReviewTime}\"," +
                    "\"${note.nextReviewTimeReversed}\""

        override fun noteToStringSimplified(note: Note): String =
            "\"${note.english}\"," +
                    "\"${note.japanese}\"," +
                    "\"${note.reading}\""
    }
}