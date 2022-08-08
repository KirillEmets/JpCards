package com.kirillemets.flashcards.android.domain.service

interface TextToSpeechService {
    fun speak(text: String): Boolean
}