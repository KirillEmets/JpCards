package com.kirillemets.flashcards.android.data.export

import android.Manifest
import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.kirillemets.flashcards.shared.domain.model.ExportInfo
import com.kirillemets.flashcards.shared.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream

class ToFileExportPerformer(private val context: Context) : ExportPerformer() {
    override suspend fun performExport(notes: List<Note>, exportInfo: ExportInfo) {
        try {
            withContext(Dispatchers.IO) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveFileQ(notes, exportInfo)
                } else
                    saveFileLegacy(notes, exportInfo)

                Toast.makeText(context, "File saved", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @TargetApi(29)
    private fun saveFileQ(notes: List<Note>, exportInfo: ExportInfo) {
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, exportInfo.filename)
            put(
                MediaStore.Downloads.MIME_TYPE,
                MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(exportInfo.exportSchema.fileExtension)
            )
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            val os: OutputStream = context.contentResolver.openOutputStream(it)!!
            writeToStream(notes, exportInfo, os)
            os.close()

            values.clear()
            values.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(it, values, null, null)
        } ?: throw RuntimeException("MediaStore failed for some reason")
    }

    @Suppress("DEPRECATION")
    private fun saveFileLegacy(notes: List<Note>, exportInfo: ExportInfo) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "${exportInfo.filename}.csv"
        )
        val os: OutputStream = file.outputStream()
        writeToStream(notes, exportInfo, os)
        os.close()
    }
}