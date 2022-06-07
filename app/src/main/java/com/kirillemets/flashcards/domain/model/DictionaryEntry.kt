package com.kirillemets.flashcards.domain.model

class DictionaryEntry(
    val japanese: String,
    val reading: String,
    val englishMeanings: List<String>,
) {
    val meaningsString: String
        get() = englishMeanings.joinToString().take(50)

    fun meaningString(id: Int) = (id + 1).toString() + ". " + englishMeanings[id]
}