package com.kirillemets.flashcards.myDictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.FlashCardRepository
import com.kirillemets.flashcards.importExport.ImportFragmentViewModel

class MyDictionaryFragmentViewModelFactory(private val flashCardRepository: FlashCardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyDictionaryFragmentViewModel::class.java))
            return MyDictionaryFragmentViewModel(flashCardRepository = flashCardRepository) as T
        else throw Exception("${modelClass.name} is not assignable from MyDictionaryFragmentViewModel")
    }
}