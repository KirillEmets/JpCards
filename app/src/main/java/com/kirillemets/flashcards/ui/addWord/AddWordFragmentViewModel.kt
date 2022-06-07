package com.kirillemets.flashcards.ui.addWord

import androidx.lifecycle.*
import com.kirillemets.flashcards.domain.model.DictionaryEntry
import com.kirillemets.flashcards.domain.model.toNote
import com.kirillemets.flashcards.domain.repository.NoteRepository
import com.kirillemets.flashcards.domain.usecase.FindWordsInDictionaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddWordFragmentViewModel @Inject constructor(
    private val findWordsInDictionaryUseCase: FindWordsInDictionaryUseCase,
    private val noteRepository: NoteRepository
) :
    ViewModel() {

    private var searchJob: Job = Job()

    private val _entries: MutableStateFlow<List<DictionaryEntry>> = MutableStateFlow(listOf())
    val entries: StateFlow<List<DictionaryEntry>>
        get() = _entries

    private val _insertionResult: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val insertionResult: StateFlow<Boolean?>
        get() = _insertionResult

    fun startSearch(word: String, withDelay: Boolean = true) {
        searchJob.cancel(CancellationException())
        searchJob = viewModelScope.launch {
            if (withDelay)
                delay(500)
            try {
                _entries.value = findWordsInDictionaryUseCase(word)
            } catch (e: CancellationException) {

            } catch (e: Exception) {
                // TODO
                _entries.value = listOf(DictionaryEntry(e.message ?: "", "error", listOf()))
            }
        }
    }

    fun onAddButtonClicked(dictionaryEntry: DictionaryEntry, id: Int) {
        viewModelScope.launch {
            _insertionResult.value = noteRepository.insertNew(dictionaryEntry.toNote(id))
        }
    }
}
