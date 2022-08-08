package com.kirillemets.flashcards.android.domain.model

sealed class ExportDestination {
    object SaveToFile : ExportDestination()
    object Share : ExportDestination()
}