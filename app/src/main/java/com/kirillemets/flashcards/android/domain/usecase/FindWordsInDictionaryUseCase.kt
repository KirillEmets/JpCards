package com.kirillemets.flashcards.android.domain.usecase

import com.kirillemets.flashcards.android.domain.model.DictionaryEntry
import com.kirillemets.flashcards.android.domain.repository.DictionaryRepository
import java.util.*
import javax.inject.Inject

class FindWordsInDictionaryUseCase @Inject constructor(private val dictionaryRepository: DictionaryRepository) {
    suspend operator fun invoke(queue: String): List<DictionaryEntry> {
        return dictionaryRepository.findWords(queue.lowercase(Locale.getDefault()))
    }
}