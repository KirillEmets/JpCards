package com.kirillemets.flashcards.shared.domain.repository

import com.kirillemets.flashcards.shared.domain.model.DictionaryEntry

interface DictionaryRepository {
    suspend fun findWords(word: String): List<DictionaryEntry>
}