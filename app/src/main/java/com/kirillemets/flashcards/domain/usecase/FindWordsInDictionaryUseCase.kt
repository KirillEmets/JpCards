package com.kirillemets.flashcards.domain.usecase

import com.kirillemets.flashcards.domain.model.DictionaryEntry
import com.kirillemets.flashcards.domain.repository.DictionaryRepository
import javax.inject.Inject

class FindWordsInDictionaryUseCase @Inject constructor(private val dictionaryRepository: DictionaryRepository) {
    suspend operator fun invoke(queue: String): List<DictionaryEntry> {
        return dictionaryRepository.findWords(queue)
    }
}