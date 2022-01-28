package com.kirillemets.flashcards.model

import com.kirillemets.flashcards.addWord.SearchResultCard
import com.kirillemets.flashcards.data.apiService.QueryData

fun QueryData.toSearchResultCards(): List<SearchResultCard> {
    val list: MutableList<SearchResultCard> = mutableListOf()
    var japanese: String
    var reading: String
    var englishMeanings: List<String>

    data.forEach { word ->
        reading = word.japanese?.get(0)?.reading ?: ""
        japanese = word.japanese?.get(0)?.word ?: reading
        if (japanese == reading)
            reading = ""

        englishMeanings = word.senses?.map { sense ->
            sense.english_definitions.joinToString()
        } ?: listOf()

        list.add(
            SearchResultCard(
                japanese,
                reading,
                englishMeanings
            )
        )
    }
    val maxSize = 25
    if (list.size > maxSize)
        return list.subList(0, maxSize)
    return list
}