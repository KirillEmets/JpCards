package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.service.TextToSpeechService

class SpeakTextUseCase constructor(private val textToSpeechService: TextToSpeechService) {
    operator fun invoke(text: String): Boolean {
        return textToSpeechService.speak(text)
    }
}