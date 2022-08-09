package com.kirillemets.flashcards.shared.domain.service

interface TextToSpeechService {
    fun speak(text: String): Boolean
}