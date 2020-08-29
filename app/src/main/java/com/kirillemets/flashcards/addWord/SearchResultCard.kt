package com.kirillemets.flashcards.addWord

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

class SearchResultCard(
    val japanese: String,
    val reading: String,
    val englishMeanings: List<String>,
) {
    val meaningsString: String
        get() = englishMeanings.joinToString().take(50)
}