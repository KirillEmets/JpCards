package com.kirillemets.flashcards.android.data.export

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.kirillemets.flashcards.android.domain.model.ExportInfo
import com.kirillemets.flashcards.android.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream

class ShareExportPerformer(private val context: Context) : ExportPerformer() {
    override suspend fun performExport(notes: List<Note>, exportInfo: ExportInfo) {
        try {
            val uri: Uri = saveToLocal(notes, exportInfo)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                flags =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            }

            val chooser = Intent.createChooser(intent, "Share")
            chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooser)

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun saveToLocal(
        cards: List<Note>,
        exportInfo: ExportInfo
    ): Uri {
        val file = withContext(Dispatchers.IO) {
            val exports = File(context.filesDir, "exports")
            exports.mkdir()
            val files = exports.listFiles()
            files?.forEach {
                it.delete()
                Log.i("EXPORTER", "Removed ${it.absolutePath}")
            }

            val file =
                File(exports, "${exportInfo.filename}.${exportInfo.exportSchema.fileExtension}")
            val os: OutputStream = file.outputStream()
            writeToStream(cards, exportInfo, os)
            os.close()

            file
        }

        return FileProvider.getUriForFile(context, "com.kirillemets.fileprovider", file)
    }
}