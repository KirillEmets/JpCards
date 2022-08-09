package com.kirillemets.flashcards.shared.domain.model

data class Note(
    val noteId: Int,
    val japanese: String,
    val reading: String,
    val english: String,
    val lastDelay: Int,
    val nextReviewTime: Long,
    val lastDelayReversed: Int,
    val nextReviewTimeReversed: Long
)