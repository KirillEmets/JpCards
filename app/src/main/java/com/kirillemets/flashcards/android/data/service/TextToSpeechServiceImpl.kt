package com.kirillemets.flashcards.android.data.service

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.kirillemets.flashcards.shared.domain.service.TextToSpeechService
import java.util.*
import javax.inject.Inject

class TextToSpeechServiceImpl @Inject constructor(appContext: Context) : TextToSpeechService {
    var ready = false
    private val tts = TextToSpeech(appContext) {
        ready = true
    }

    override fun speak(text: String): Boolean {
        tts.language = Locale.JAPANESE

        if (!ready)
            return false

        val result =
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle(), UUID.randomUUID().toString())
        return result == TextToSpeech.SUCCESS

    }
}