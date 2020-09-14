package com.kirillemets.flashcards

import org.joda.time.LocalDate

class TimeUtil {
    companion object {
        val todayMillis: Long
            get() = LocalDate.now().toDateTimeAtStartOfDay().millis
    }
}