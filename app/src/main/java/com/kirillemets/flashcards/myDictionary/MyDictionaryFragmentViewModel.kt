package com.kirillemets.flashcards.myDictionary

import androidx.lifecycle.*
import com.kirillemets.flashcards.TimeUtil
import com.kirillemets.flashcards.database.FlashCard
import com.kirillemets.flashcards.database.FlashCardRepository
import kotlinx.coroutines.*

class MyDictionaryFragmentViewModel(val flashCardRepository: FlashCardRepository): ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private val allCards: LiveData<List<FlashCard>> =
        flashCardRepository.getAll()

    // TODO Make it the normal way
    private val cardsObserver = Observer<List<FlashCard>> {
            cards ->
        _displayedCards.postValue(cards)
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

    var lastFilter: String? = null

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
        coroutineScope.launch(Dispatchers.IO) {
            flashCardRepository.deleteByIndexes(ids)
        }
    }

    fun resetWords(ids: Set<Int>) {
        coroutineScope.launch(Dispatchers.IO) {
            flashCardRepository.resetDelayByIds(ids, TimeUtil.todayMillis)
            flashCardRepository.resetDelayByIdsReversed(ids, TimeUtil.todayMillis)
        }
    }

    override fun onCleared() {
        super.onCleared()
        allCards.removeObserver(cardsObserver)
        job.cancel(CancellationException("Cancelled on onCleared"))
    }
}