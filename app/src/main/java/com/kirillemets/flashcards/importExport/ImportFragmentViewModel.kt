package com.kirillemets.flashcards.importExport

import androidx.lifecycle.*
import com.kirillemets.flashcards.database.FlashCardRepository
import com.kirillemets.flashcards.database.FlashCard

class ImportFragmentViewModel(flashCardRepository: FlashCardRepository): ViewModel() {
    private val _importedCards: MutableLiveData<List<FlashCard>> = MutableLiveData()

    val importedCards: LiveData<List<FlashCard>>
        get() = _importedCards

    val listSizeText = Transformations.map(importedCards) {
        "Flashcards in file: ${it.size}"
    }

    fun setImportedCards(cards: List<FlashCard>) {
        _importedCards.value = cards
    }
}

class ImportFragmentViewModelFactory (private val flashCardRepository: FlashCardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ImportFragmentViewModel::class.java))
            return ImportFragmentViewModel(flashCardRepository = flashCardRepository) as T
        else throw Exception("${modelClass.name} is not assignable from ImportFragmentViewModel")
    }
}