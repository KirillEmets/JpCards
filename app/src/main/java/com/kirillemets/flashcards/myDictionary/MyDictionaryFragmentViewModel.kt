package com.kirillemets.flashcards.myDictionary

import android.util.Log
import androidx.lifecycle.*
import com.kirillemets.flashcards.TimeUtil
import com.kirillemets.flashcards.model.FlashCard
import com.kirillemets.flashcards.model.FlashCardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyDictionaryFragmentViewModel @Inject constructor(val flashCardRepository: FlashCardRepository) :
    ViewModel() {
    private val allCards: LiveData<List<FlashCard>> =
        flashCardRepository.getAll()

    private val cardsObserver = Observer<List<FlashCard>> { cards ->
        _displayedCards.value = cards
        lastFilter?.let { filterWords(it) }
    }

    init {
        allCards.observeForever(cardsObserver)
    }

    private val _displayedCards: MutableLiveData<List<FlashCard>> = MutableLiveData()
    val displayedCards: LiveData<List<FlashCard>>
        get() = _displayedCards

    val countText: LiveData<String> = Transformations.map(displayedCards) {
        "Word count: ${it.size}"
    }

    private var lastFilter: String? = null

    fun filterWords(query: String) {
        lastFilter = query
        allCards.value?.filter { card ->
            card.english.contains(query, true)
                    || card.japanese.contains(query, true)
                    || card.reading.contains(query, true)
        }?.let {
            _displayedCards.value = it
        }
    }

    fun deleteWords(ids: Set<Int>) {
        flashCardRepository.deleteByIndexes(ids)

    }

    fun resetWords(ids: Set<Int>) {
        flashCardRepository.resetDelayByIds(ids, TimeUtil.todayMillis)
        flashCardRepository.resetDelayByIdsReversed(ids, TimeUtil.todayMillis)
    }

    override fun onCleared() {
        super.onCleared()
        allCards.removeObserver(cardsObserver)
    }
}