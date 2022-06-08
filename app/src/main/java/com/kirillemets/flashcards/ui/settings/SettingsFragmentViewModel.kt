package com.kirillemets.flashcards.ui.settings

import androidx.lifecycle.*
import com.kirillemets.flashcards.model.FlashCardRepository
import com.kirillemets.flashcards.data.model.FlashCard
import com.kirillemets.flashcards.domain.model.ExportInfo
import com.kirillemets.flashcards.domain.usecase.ExportNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentViewModel @Inject constructor(
    private val exportNotesUseCase: ExportNotesUseCase,
    val flashCardRepository: FlashCardRepository
) :
    ViewModel() {
    private val _importedCards: MutableLiveData<List<FlashCard>> = MutableLiveData()

    val importedCards: LiveData<List<FlashCard>>
        get() = _importedCards

    fun setImportedCards(cards: List<FlashCard>) {
        _importedCards.value = cards
    }

    fun overrideCards() {
        importedCards.value?.let {
            flashCardRepository.deleteAll()
            flashCardRepository.insert(it)
        }
    }

    fun addCards() {
        importedCards.value?.let {
            flashCardRepository.insert(it)
        }
    }

    fun exportNotes(exportInfo: ExportInfo) {
        viewModelScope.launch {
            exportNotesUseCase(exportInfo)
        }
    }
}
