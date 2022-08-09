package com.kirillemets.flashcards.android.data.service

import android.content.Context
import com.kirillemets.flashcards.android.data.export.ShareExportPerformer
import com.kirillemets.flashcards.android.data.export.ToFileExportPerformer
import com.kirillemets.flashcards.shared.domain.model.ExportDestination
import com.kirillemets.flashcards.shared.domain.model.ExportInfo
import com.kirillemets.flashcards.shared.domain.model.Note
import com.kirillemets.flashcards.shared.domain.service.NoteExporterService
import javax.inject.Inject

class NoteExporterServiceImpl @Inject constructor(
    private val context: Context
) : NoteExporterService {
    override suspend fun exportNotes(notes: List<Note>, exportInfo: ExportInfo) {
        val performer = when(exportInfo.exportDestination) {
            ExportDestination.SaveToFile -> ToFileExportPerformer(context)
            ExportDestination.Share -> ShareExportPerformer(context)
        }

        performer.performExport(notes, exportInfo)
    }
}