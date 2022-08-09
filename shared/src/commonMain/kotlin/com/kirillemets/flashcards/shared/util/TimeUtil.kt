package com.kirillemets.flashcards.shared.util

expect class TimeUtil {
    companion object {
        fun getCurrentTimeMillis(): Long
    }
}