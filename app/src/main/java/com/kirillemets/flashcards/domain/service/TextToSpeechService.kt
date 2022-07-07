package com.kirillemets.flashcards.domain.service

interface TextToSpeechService {
    fun speak(text: String): Boolean
}