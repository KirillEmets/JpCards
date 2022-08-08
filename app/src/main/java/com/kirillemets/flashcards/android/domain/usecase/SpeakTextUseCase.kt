package com.kirillemets.flashcards.android.domain.usecase

import com.kirillemets.flashcards.android.domain.service.TextToSpeechService
import javax.inject.Inject

class SpeakTextUseCase @Inject constructor(private val textToSpeechService: TextToSpeechService) {
    operator fun invoke(text: String): Boolean {
        return textToSpeechService.speak(text)
    }
}