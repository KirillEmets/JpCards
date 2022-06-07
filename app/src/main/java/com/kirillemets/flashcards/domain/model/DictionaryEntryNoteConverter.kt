package com.kirillemets.flashcards.domain.model

fun DictionaryEntry.toNote(meaningId: Int) = Note(0, japanese, reading, englishMeanings[meaningId], 0, 0, 0, 0)