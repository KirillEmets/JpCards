package com.kirillemets.flashcards.myDictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kirillemets.flashcards.TimeUtil
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import kotlinx.coroutines.*
import org.joda.time.LocalDate

class MyDictionaryFragmentViewModel(val database: CardDatabaseDao): ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private val allCards: LiveData<List<FlashCard>> =
        database.getAll()

    // TODO Make it the normal way
    init {
        allCards.observeForever { cards ->
            _displayedCards.postValue(cards)
            lastFilter?.let { filterWords(it) }
        }
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
            database.deleteByIndexes(ids)
        }
    }

    fun resetWords(ids: Set<Int>) {
        coroutineScope.launch(Dispatchers.IO) {
            database.resetDelayByIds(ids, TimeUtil.todayMillis)
            database.resetDelayByIdsReversed(ids, TimeUtil.todayMillis)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel(CancellationException("Cancelled on onCleared"))
    }
}