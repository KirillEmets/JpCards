package com.kirillemets.flashcards.importExport

import com.kirillemets.flashcards.model.FlashCard
import org.junit.Assert.*
import org.junit.Test

class CSVExporterTest {
    @Test
    fun testWithProgress() {
        val exporter = CSVExporter(withProgress = true)
        val card = FlashCard(0, "Jap", "reading", "Eng", 1, 0, 1, 0)
        val string = exporter.convertToString(card)
        assertEquals("\"Eng\",\"Jap\",\"reading\",\"1\",\"1\",\"0\",\"0\"", string)
    }

    @Test
    fun testWithoutProgress() {
        val exporter = CSVExporter(withProgress = false)
        val card = FlashCard(0, "Jap", "reading", "Eng", 1, 0, 1, 0)
        val string = exporter.convertToString(card)
        assertEquals("\"Eng\",\"Jap\",\"reading\"", string)
    }

    @Test
    fun testExportCards() {
        val exporter = CSVExporter(withProgress = false)
        val c1 = FlashCard(0, "Jap1", "reading1", "Eng1", 1, 0, 1, 0)
        val c2 = FlashCard(0, "Jap2", "reading2", "Eng2", 1, 0, 1, 0)
        exporter.exportCards(listOf(c1, c2), System.out)
    }
}