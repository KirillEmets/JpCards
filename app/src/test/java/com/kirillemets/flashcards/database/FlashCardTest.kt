package com.kirillemets.flashcards.database

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

data class RemainingTimeTestData(val lastDelay: Int, val lastRepeatDateString: String, val currentTimeString: String, val expextedValue: Int)
class FlashCardTest {
    private val tests = listOf(
        RemainingTimeTestData(4, "01-06-2020", "05-06-2020", 0),
        RemainingTimeTestData(6, "01-06-2020", "07-06-2020", 0),
        RemainingTimeTestData(5, "01-06-2020", "01-06-2020", 5),
        RemainingTimeTestData(1, "01-06-2020", "03-06-2020", 0),
        RemainingTimeTestData(1, "01-06-2020", "02-06-2020", 0),
    )

    @Test
    fun getRemainingTime() {
        var card: FlashCard
        var lastRepeatTime: Long = 0

        tests.forEachIndexed { i, test ->
            lastRepeatTime = SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(test.lastRepeatDateString)?.time ?: 0
            card = FlashCard(0, "", "", "", test.lastDelay, lastRepeatTime)

            val currentTime: Long =
                SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(test.currentTimeString)?.time ?: 0

            val remaining: Int = card.getRemainingTime(currentTime)
            assertEquals("test $i:", test.expextedValue, remaining)
        }
    }
}