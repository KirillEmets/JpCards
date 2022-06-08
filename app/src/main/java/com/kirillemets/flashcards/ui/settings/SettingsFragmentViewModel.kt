package com.kirillemets.flashcards.ui.settings

import androidx.lifecycle.*
import com.kirillemets.flashcards.model.FlashCardRepository
import com.kirillemets.flashcards.data.model.FlashCard
import com.kirillemets.flashcards.data.model.toNotes
import com.kirillemets.flashcards.domain.model.ExportInfo
import com.kirillemets.flashcards.domain.model.Note
import com.kirillemets.flashcards.domain.usecase.AddNotesUseCase
import com.kirillemets.flashcards.domain.usecase.DeleteAllNotesUseCase
import com.kirillemets.flashcards.domain.usecase.ExportNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentViewModel @Inject constructor(
    private val exportNotesUseCase: ExportNotesUseCase,
    private val deleteAllNotesUseCase: DeleteAllNotesUseCase,
    private val addNotesUseCase: AddNotesUseCase
) :
    ViewModel() {
    private var _importedCards: List<Note> = listOf()

    val importedCards: List<Note>
        get() = _importedCards

    fun setImportedCards(cards: List<Note>) {
        _importedCards = cards
    }

    fun overrideCards() {
        if (importedCards.isEmpty())
            return

        viewModelScope.launch {
            deleteAllNotesUseCase()
            addNotesUseCase(importedCards)
        }
    }

    fun addCards() {
        if (importedCards.isEmpty())
            return

        viewModelScope.launch {
            addNotesUseCase(importedCards)
        }
    }

    fun exportNotes(exportInfo: ExportInfo) {
        viewModelScope.launch {
            exportNotesUseCase(exportInfo)
        }
    }
}
