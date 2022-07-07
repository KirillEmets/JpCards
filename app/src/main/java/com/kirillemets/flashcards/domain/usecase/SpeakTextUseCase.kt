package com.kirillemets.flashcards.domain.usecase

import com.kirillemets.flashcards.domain.service.TextToSpeechService
import javax.inject.Inject

class SpeakTextUseCase @Inject constructor(private val textToSpeechService: TextToSpeechService) {
    operator fun invoke(text: String): Boolean {
        return textToSpeechService.speak(text)
    }
}