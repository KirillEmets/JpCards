package com.kirillemets.flashcards.shared.domain.usecase

import com.kirillemets.flashcards.shared.domain.model.DictionaryEntry
import com.kirillemets.flashcards.shared.domain.repository.DictionaryRepository

class FindWordsInDictionaryUseCase constructor(private val dictionaryRepository: DictionaryRepository) {
    suspend operator fun invoke(queue: String): List<DictionaryEntry> {
        return dictionaryRepository.findWords(queue.lowercase())
    }
}