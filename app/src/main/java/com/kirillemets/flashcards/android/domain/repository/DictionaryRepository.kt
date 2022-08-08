package com.kirillemets.flashcards.android.domain.repository

import com.kirillemets.flashcards.android.domain.model.DictionaryEntry

interface DictionaryRepository {
    suspend fun findWords(word: String): List<DictionaryEntry>
}