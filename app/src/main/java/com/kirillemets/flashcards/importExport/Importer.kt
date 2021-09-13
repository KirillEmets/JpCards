package com.kirillemets.flashcards.importExport

import com.kirillemets.flashcards.model.FlashCard
import java.io.InputStream

abstract class Importer {
    abstract fun import(inputStream: InputStream): List<FlashCard>
}

class CSVImporter : Importer() {
    private val regex = Regex("^\"|\",\"|\"$")

    private fun csvToCard(csv: String): FlashCard {
        val fields = regex.split(csv).let { it.subList(1, it.size - 1) }
        if (fields.size != 3 && fields.size != 7)
            throw Exception("Wrong file format. CSV must have 3 or 7 fields")

        val eng = fields[0]
        val jap = fields[1]
        val reading = fields[2]

        if (fields.size == 3) {
            return FlashCard(0, jap, reading, eng)
        } else {
            val lastDelay = fields[3].toIntOrNull()
                ?: throw Exception("Failed parsing field 4 - lastDelay in $csv")
            val lastDelayReversed = fields[4].toIntOrNull()
                ?: throw Exception("Failed parsing field 5 - lastDelayReversed in $csv")
            val nextTime = fields[5].toLongOrNull()
                ?: throw Exception("Failed parsing field 6 - nextTime in $csv")
            val nextTimeReversed = fields[6].toLongOrNull()
                ?: throw Exception("Failed parsing field 7 - nextTimeReversed in $csv")

            return FlashCard(
                0,
                japanese = jap,
                reading = reading,
                english = eng,
                lastDelay = lastDelay,
                lastDelayReversed = lastDelayReversed,
                nextReviewTime = nextTime,
                nextReviewTimeReversed = nextTimeReversed
            )
        }
    }

    override fun import(inputStream: InputStream): List<FlashCard> {
        val rawStrings = String(inputStream.readBytes()).splitToSequence("\n")
        val cards = mutableListOf<FlashCard>()

        rawStrings.forEach { rawCard ->
            cards += csvToCard(rawCard)
        }

        return cards
    }
}