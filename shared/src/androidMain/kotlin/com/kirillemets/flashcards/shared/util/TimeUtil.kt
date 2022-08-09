package com.kirillemets.flashcards.shared.util


actual class TimeUtil {
    actual companion object {
        actual fun getCurrentTimeMillis(): Long {
            return System.currentTimeMillis()
        }
    }
}