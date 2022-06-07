package com.kirillemets.flashcards.domain.repository

import com.kirillemets.flashcards.domain.model.DictionaryEntry

interface DictionaryRepository {
    suspend fun findWords(word: String): List<DictionaryEntry>
}