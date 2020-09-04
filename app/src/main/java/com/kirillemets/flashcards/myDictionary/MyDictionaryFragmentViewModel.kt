package com.kirillemets.flashcards.myDictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyDictionaryFragmentViewModel(val database: CardDatabaseDao): ViewModel() {

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    val allCards: LiveData<List<FlashCard>> =
        database.getAll()

    fun deleteWords(ids: Set<Int>) {
        coroutineScope.launch(Dispatchers.IO) {
            database.deleteByIndexes(ids)
        }
    }

    fun resetWords(ids: Set<Int>) {
        coroutineScope.launch(Dispatchers.IO) {
            database.resetDelayByIndexes(ids)
        }
    }
}