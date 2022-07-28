package com.kirillemets.flashcards.domain.usecase

import androidx.compose.ui.text.toLowerCase
import com.kirillemets.flashcards.domain.model.DictionaryEntry
import com.kirillemets.flashcards.domain.repository.DictionaryRepository
import java.util.*
import javax.inject.Inject

class FindWordsInDictionaryUseCase @Inject constructor(private val dictionaryRepository: DictionaryRepository) {
    suspend operator fun invoke(queue: String): List<DictionaryEntry> {
        return dictionaryRepository.findWords(queue.lowercase(Locale.getDefault()))
    }
}