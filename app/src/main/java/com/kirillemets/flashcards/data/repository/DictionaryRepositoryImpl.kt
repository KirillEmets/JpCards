package com.kirillemets.flashcards.data.repository

import com.kirillemets.flashcards.data.apiService.JishoApiService
import com.kirillemets.flashcards.domain.model.DictionaryEntry
import com.kirillemets.flashcards.domain.repository.DictionaryRepository
import com.kirillemets.flashcards.model.toDictionaryEntries
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(private val jishoApiService: JishoApiService) : DictionaryRepository {
    override suspend fun findWords(word: String): List<DictionaryEntry> {
        return jishoApiService.getQueryData(word).toDictionaryEntries()
    }
}
