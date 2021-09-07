package com.kirillemets.flashcards.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kirillemets.flashcards.database.FlashCardRepository
import com.kirillemets.flashcards.myDictionary.MyDictionaryFragmentViewModel

class ReviewFragmentViewModelFactory (private val flashCardRepository: FlashCardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReviewFragmentViewModel::class.java))
            return ReviewFragmentViewModel(flashCardRepository = flashCardRepository) as T
        else throw Exception("${modelClass.name} is not assignable from ReviewFragmentViewModel")    }
}