package com.kirillemets.flashcards.domain

interface AppPreferences {
    var delayMissMultiplier: Float
    var delayEasyMultiplier: Float
    var delayHardMultiplier: Float
}