package com.kirillemets.flashcards.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kirillemets.flashcards.ui.TimeUtil
import org.joda.time.LocalDate
import java.util.concurrent.TimeUnit

@Entity
data class FlashCard(
    @PrimaryKey(autoGenerate = true) val cardId: Int,
    val japanese: String,
    val reading: String,
    val english: String,
    @ColumnInfo(name = "last_delay")
    val lastDelay: Int = 1,
    @ColumnInfo(name = "next_review_time")
    val nextReviewTime: Long = TimeUtil.todayMillis,
    @ColumnInfo(name = "last_delay_reversed")
    val lastDelayReversed: Int = 1,
    @ColumnInfo(name = "next_review_time_reversed")
    val nextReviewTimeReversed: Long = TimeUtil.todayMillis
) {
    fun getRemainingTimes(
        currentDate: Long = LocalDate.now().toDateTimeAtStartOfDay().millis
    ): Pair<Int, Int> {
        return TimeUnit.DAYS.convert(nextReviewTime - currentDate, TimeUnit.MILLISECONDS)
            .toInt().coerceAtLeast(0) to
                TimeUnit.DAYS.convert(nextReviewTimeReversed - currentDate, TimeUnit.MILLISECONDS)
                    .toInt().coerceAtLeast(0)
    }
}