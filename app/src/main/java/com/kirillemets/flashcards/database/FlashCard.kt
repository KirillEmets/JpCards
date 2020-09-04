package com.kirillemets.flashcards.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

@Entity
data class FlashCard(
    @PrimaryKey(autoGenerate = true) val cardId: Int,
    val japanese: String,
    val reading: String,
    val english: String,
    @ColumnInfo(name = "last_delay") val lastDelay: Int = 1,
    @ColumnInfo(name = "last_repeat_time") val lastRepeatTime: Long = GregorianCalendar().timeInMillis
    ) {
    fun getRemainingTime(currentDate: Long = GregorianCalendar().timeInMillis): Int {
        return (lastDelay - TimeUnit.DAYS.convert(currentDate - lastRepeatTime, TimeUnit.MILLISECONDS)
            .toInt()).coerceAtLeast(0)
    }
}