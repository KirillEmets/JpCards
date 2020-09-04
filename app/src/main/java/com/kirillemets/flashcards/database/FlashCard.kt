package com.kirillemets.flashcards.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import java.util.concurrent.TimeUnit

@Entity
data class FlashCard(
    @PrimaryKey(autoGenerate = true) val cardId: Int,
    val japanese: String,
    val reading: String,
    val english: String,
    @ColumnInfo(name = "last_delay") val lastDelay: Int = 0,
    @ColumnInfo(name = "next_review_time") val nextReviewTime: Long = GregorianCalendar().timeInMillis,
    @ColumnInfo(name = "last_delay_reversed") val lastDelayReversed: Int = 0,
    @ColumnInfo(name = "next_review_time_reversed") val nextReviewTimeReversed: Long = GregorianCalendar().timeInMillis
    ) {
    fun getRemainingTime(currentDate: Long = GregorianCalendar().timeInMillis): Pair<Int, Int> {
        return TimeUnit.DAYS.convert(nextReviewTime - currentDate,TimeUnit.MILLISECONDS)
            .toInt().coerceAtLeast(0) to
                TimeUnit.DAYS.convert(nextReviewTimeReversed - currentDate, TimeUnit.MILLISECONDS)
                    .toInt().coerceAtLeast(0)
    }
}