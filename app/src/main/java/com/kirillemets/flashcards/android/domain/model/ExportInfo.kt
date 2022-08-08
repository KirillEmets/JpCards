package com.kirillemets.flashcards.android.domain.model

data class ExportInfo(
    val exportDestination: ExportDestination,
    val withProgress: Boolean,
    val filename: String,
    val exportSchema: ExportSchema
)