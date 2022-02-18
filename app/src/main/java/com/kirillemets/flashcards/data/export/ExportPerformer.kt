package com.kirillemets.flashcards.data.export

import com.kirillemets.flashcards.domain.model.ExportInfo
import com.kirillemets.flashcards.domain.model.Note
import java.io.OutputStream

abstract class ExportPerformer {
    protected fun writeToStream(notes: List<Note>, exportInfo: ExportInfo, os: OutputStream) {
        val exportSchema = exportInfo.exportSchema
        for (i in notes.indices) {
            os.write(
                exportSchema.noteToString(notes[i], exportInfo.withProgress).toByteArray()
            )
            if (i != notes.lastIndex)
                os.write('\n'.code)
        }
    }

    abstract suspend fun performExport(notes: List<Note>, exportInfo: ExportInfo)
}