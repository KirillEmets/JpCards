package com.kirillemets.flashcards.addWord

import com.kirillemets.flashcards.model.FlashCard

class SearchResultCard(
    val japanese: String,
    val reading: String,
    val englishMeanings: List<String>,
) {
    val meaningsString: String
        get() = englishMeanings.joinToString().take(50)

    fun meaningString(id: Int) = (id+1).toString() + ". " + englishMeanings[id]
    fun flashCard(translationId: Int) =
        FlashCard(0, japanese, reading, englishMeanings[translationId])
}