package com.kirillemets.flashcards.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.database.CardDatabaseDao

class ReviewFragmentViewModelFactory (val database: CardDatabaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CardDatabaseDao::class.java).newInstance(database)
    }
}