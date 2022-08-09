package com.kirillemets.flashcards.android.ui.settings

import androidx.lifecycle.*
import com.kirillemets.flashcards.shared.domain.model.ExportInfo
import com.kirillemets.flashcards.shared.domain.model.Note
import com.kirillemets.flashcards.shared.domain.usecase.AddNotesUseCase
import com.kirillemets.flashcards.shared.domain.usecase.DeleteAllNotesUseCase
import com.kirillemets.flashcards.shared.domain.usecase.ExportNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
