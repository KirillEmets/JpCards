package com.kirillemets.flashcards.importExport

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider.getUriForFile
import com.kirillemets.flashcards.data.model.FlashCard
import java.io.File
import java.io.OutputStream

data class ExportInfo(
    val cards: List<FlashCard>,
    val exporter: Exporter,
    val name: String,
    val context: Context
)

abstract class Exporter {
    abstract fun exportCards(cards: List<FlashCard>, outputStream: OutputStream)
    abstract fun getExtension(): String
}

class CSVExporter(withProgress: Boolean) : Exporter() {
    val convertToString = if (withProgress) ::cardToString else ::cardToStringSimplified

    override fun exportCards(cards: List<FlashCard>, outputStream: OutputStream) {
        for (i in cards.indices) {
            outputStream.write(convertToString(cards[i]).toByteArray())
            if (i != cards.lastIndex)
                outputStream.write('\n'.code)
        }
    }

    override fun getExtension() = "csv"

    private fun cardToString(card: FlashCard): String =
        "\"${card.english}\"," +
                "\"${card.japanese}\"," +
                "\"${card.reading}\"," +
                "\"${card.lastDelay}\"," +
                "\"${card.lastDelayReversed}\"," +
                "\"${card.nextReviewTime}\"," +
                "\"${card.nextReviewTimeReversed}\""

    private fun cardToStringSimplified(card: FlashCard): String =
        "\"${card.english}\"," +
                "\"${card.japanese}\"," +
                "\"${card.reading}\""
}

fun saveToLocal(
    cards: List<FlashCard>,
    filename: String,
    exporter: Exporter,
    context: Context
): Uri {
    val exports = File(context.filesDir, "exports")
    exports.mkdir()
    val files = exports.listFiles()
    files?.forEach {
        it.delete()
        Log.i("EXPORTER", "Removed ${it.absolutePath}")
    }

    val file = File(exports, "${filename}.${exporter.getExtension()}")
    val os: OutputStream = file.outputStream()
    exporter.exportCards(cards, os)
    os.close()

    return getUriForFile(context, "com.kirillemets.fileprovider", file)
}

fun exportToStorage(exportInfo: ExportInfo) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveFileQ(exportInfo)
    } else
        saveFileLegacy(exportInfo)
}

@TargetApi(29)
private fun saveFileQ(exportInfo: ExportInfo) {
    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, exportInfo.name)
        put(
            MediaStore.Downloads.MIME_TYPE,
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(exportInfo.exporter.getExtension())
        )
        put(MediaStore.Downloads.IS_PENDING, 1)
    }

    val resolver = exportInfo.context.contentResolver
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        val os: OutputStream = exportInfo.context.contentResolver.openOutputStream(it)!!
        exportInfo.exporter.exportCards(exportInfo.cards, os)
        os.close()

        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(it, values, null, null)
    } ?: throw RuntimeException("MediaStore failed for some reason")
}

@Suppress("DEPRECATION")
private fun saveFileLegacy(exportInfo: ExportInfo) {
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "${exportInfo.name}.${exportInfo.exporter.getExtension()}"
    )
    val os: OutputStream = file.outputStream()
    exportInfo.exporter.exportCards(exportInfo.cards, os)
    os.close()
}