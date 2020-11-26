package com.kirillemets.flashcards.myDictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kirillemets.flashcards.TimeUtil
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import kotlinx.coroutines.*
import org.joda.time.LocalDate

class MyDictionaryFragmentViewModel(val database: CardDatabaseDao): ViewModel() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    val allCards: LiveData<List<FlashCard>> =
        database.getAll()

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