package com.kirillemets.flashcards.shared.domain.model

sealed class ExportDestination {
    object SaveToFile : ExportDestination()
    object Share : ExportDestination()
}