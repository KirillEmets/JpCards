package com.kirillemets.flashcards.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.database.CardDatabaseDao
import com.kirillemets.flashcards.database.DatabaseRepository

class ReviewFragmentViewModelFactory (val repository: DatabaseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DatabaseRepository::class.java).newInstance(repository)
    }
}