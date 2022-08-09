package com.kirillemets.flashcards.shared.domain

interface AppPreferences {
    var delayMissMultiplier: Int
    var delayEasyMultiplier: Int
    var delayHardMultiplier: Int

    var theme: Theme

    enum class Theme() {
        Auto,
        Light,
        Black
    }
}