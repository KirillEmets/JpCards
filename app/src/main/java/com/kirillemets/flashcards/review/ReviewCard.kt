package com.kirillemets.flashcards.review

data class ReviewCard(
    val japanese: String,
    val reading: String,
    val english: String,
    val reversed: Boolean,
    val cardId: Int)