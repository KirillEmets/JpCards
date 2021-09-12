package com.kirillemets.flashcards.addWord

import androidx.lifecycle.*
import com.kirillemets.flashcards.database.FlashCardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddWordFragmentViewModel @Inject constructor(val flashCardRepository: FlashCardRepository) :
    ViewModel() {

    private var searchJob: Job = Job()

    private val _flashCards: MutableLiveData<List<SearchResultCard>> = MutableLiveData(listOf())
    val flashCards: LiveData<List<SearchResultCard>>
        get() = _flashCards

    private val _insertionResult: MutableLiveData<Boolean> = MutableLiveData()
    val insertionResult: LiveData<Boolean>
        get() = _insertionResult

    fun startSearch(word: String, withDelay: Boolean = true) {
        searchJob.cancel(CancellationException())
        searchJob = viewModelScope.launch {
            if (withDelay)
                delay(500)
            try {
                _flashCards.postValue(flashCardRepository.searchWordsJisho(word))
            } catch (e: CancellationException) {

            } catch (e: Exception) {
                // TODO
                _flashCards.value = listOf(SearchResultCard(e.message ?: "", "error", listOf()))
            }
        }
    }

    fun onAddButtonClicked(resultCard: SearchResultCard, id: Int) {
        viewModelScope.launch {
            _insertionResult.value = flashCardRepository.insertNew(resultCard.flashCard(id))
        }
    }
}
