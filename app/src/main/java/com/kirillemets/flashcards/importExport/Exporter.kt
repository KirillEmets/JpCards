package com.kirillemets.flashcards.importExport

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.kirillemets.flashcards.database.FlashCard
import java.io.File
import java.io.OutputStream

abstract class Exporter {
    abstract fun exportCards(cards: List<FlashCard>, outputStream: OutputStream)
    abstract fun getExtension(): String
}

class CSVExporter(withProgress: Boolean): Exporter() {
    val convertToString = if(withProgress) ::cardToString else ::cardToStringSimplified

    override fun exportCards(cards: List<FlashCard>, outputStream: OutputStream) {
        for(i in cards.indices) {
            outputStream.write(convertToString(cards[i]).toByteArray())
            if (i != cards.lastIndex)
                outputStream.write('\n'.toInt())
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

fun exportToStorage(cards: List<FlashCard>, filename: String, exporter: Exporter, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveFileQ(cards, filename, exporter, context)
    }
    else
        saveFileLegacy(cards, filename, exporter)
}

@TargetApi(29)
private fun saveFileQ(cards: List<FlashCard>, name: String, exporter: Exporter, context: Context) {
    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, name)
        put(MediaStore.Downloads.MIME_TYPE,
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(exporter.getExtension()))
        put(MediaStore.Downloads.IS_PENDING, 1)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        val os: OutputStream = context.contentResolver.openOutputStream(it)!!
        exporter.exportCards(cards, os)
        os.close()

        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(it, values, null, null)
    } ?: throw RuntimeException("MediaStore failed for some reason")
}

@Suppress("DEPRECATION")
private fun saveFileLegacy(cards: List<FlashCard>, name: String, exporter: Exporter) {
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "$name.${exporter.getExtension()}"
    )
    val os: OutputStream = file.outputStream()
    exporter.exportCards(cards, os)
    os.close()
}