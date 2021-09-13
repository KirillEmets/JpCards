package com.kirillemets.flashcards.importExport

import androidx.lifecycle.*
import com.kirillemets.flashcards.model.FlashCardRepository
import com.kirillemets.flashcards.model.FlashCard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImportFragmentViewModel @Inject constructor(val flashCardRepository: FlashCardRepository) :
    ViewModel() {
    private val _importedCards: MutableLiveData<List<FlashCard>> = MutableLiveData()

    val importedCards: LiveData<List<FlashCard>>
        get() = _importedCards

    val listSizeText = Transformations.map(importedCards) {
        "Flashcards in file: ${it.size}"
    }

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
}
