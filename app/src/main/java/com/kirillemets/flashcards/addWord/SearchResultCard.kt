package com.kirillemets.flashcards.addWord

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kirillemets.flashcards.database.FlashCard

class SearchResultCard(
    val japanese: String,
    val reading: String,
    val englishMeanings: List<String>,
) {
    val meaningsString: String
        get() = englishMeanings.joinToString().take(50)

    fun meaningString(id: Int) = (id+1).toString() + ". " + englishMeanings[id]
    fun flashCard(translationId: Int) =
        FlashCard(0, japanese, reading, englishMeanings[translationId], 0)
}