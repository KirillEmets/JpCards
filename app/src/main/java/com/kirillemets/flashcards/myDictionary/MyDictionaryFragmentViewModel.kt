package com.kirillemets.flashcards.myDictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCard

class MyDictionaryFragmentViewModel(val database: CardDatabaseDao): ViewModel() {
    val allCards: LiveData<List<FlashCard>> =
        database.getAll()

}