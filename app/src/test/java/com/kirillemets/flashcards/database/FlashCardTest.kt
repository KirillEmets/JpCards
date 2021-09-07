package com.kirillemets.flashcards.database

import java.util.concurrent.TimeUnit

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

data class RemainingTimeTestData(val lastDelay: Int, val lastRepeatDateString: String, val currentTimeString: String, val expectedValue: Int)
class FlashCardTest {
    @Test
    fun getRemainingTime() {
        val tests = listOf(
            RemainingTimeTestData(4, "01-06-2020", "05-06-2020", 0),
            RemainingTimeTestData(6, "01-06-2020", "07-06-2020", 0),
            RemainingTimeTestData(5, "01-06-2020", "01-06-2020", 5),
            RemainingTimeTestData(1, "01-06-2020", "03-06-2020", 0),
            RemainingTimeTestData(1, "01-06-2020", "02-06-2020", 0),
        )

        var card: FlashCard
        var nextReviewTime: Long
        var lastRepeatTime: Long
        var currentTime: Long


        tests.forEachIndexed { i, test ->
            currentTime = SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(test.currentTimeString)?.time ?: 0

            lastRepeatTime = SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(test.lastRepeatDateString)?.time ?: 0

            nextReviewTime = lastRepeatTime + TimeUnit.MILLISECONDS.convert(test.lastDelay.toLong(), TimeUnit.DAYS)

            card = FlashCard(0, "", "", "", test.lastDelay, nextReviewTime, 0, 0)

            val remaining: Int = card.getRemainingTimes(currentTime).first
            assertEquals("test $i:", test.expectedValue, remaining)
        }
    }
}