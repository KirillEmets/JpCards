package com.kirillemets.flashcards.domain.model

sealed class ExportDestination {
    object SaveToFile : ExportDestination()
    object Share : ExportDestination()
}