package com.kirillemets.flashcards.android.data.repository

import com.kirillemets.flashcards.android.data.apiService.JishoApiService
import com.kirillemets.flashcards.shared.domain.model.DictionaryEntry
import com.kirillemets.flashcards.shared.domain.repository.DictionaryRepository
import com.kirillemets.flashcards.android.model.toDictionaryEntries
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(private val jishoApiService: JishoApiService) : DictionaryRepository {
    override suspend fun findWords(word: String): List<DictionaryEntry> {
        return jishoApiService.getQueryData(word).toDictionaryEntries()
    }
}
