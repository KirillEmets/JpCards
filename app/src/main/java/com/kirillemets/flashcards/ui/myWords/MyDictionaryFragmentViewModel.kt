package com.kirillemets.flashcards.ui.myWords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirillemets.flashcards.domain.model.Note
import com.kirillemets.flashcards.domain.usecase.DeleteCardsWithIndexesUseCase
import com.kirillemets.flashcards.domain.usecase.GetAllCardsUseCase
import com.kirillemets.flashcards.domain.usecase.ResetNoteProgressByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyWordsUIState(
    val notes: List<NoteUIState>,
) {
    val wordCount: Int
        get() = notes.size
}

@HiltViewModel
class MyDictionaryFragmentViewModel @Inject constructor(
    getAllCardsUseCase: GetAllCardsUseCase,
    private val deleteCardsWithIndexesUseCase: DeleteCardsWithIndexesUseCase,
    private val resetNoteProgressByIdUseCase: ResetNoteProgressByIdUseCase
) : ViewModel() {



    private val allNotes: Flow<List<Note>> = getAllCardsUseCase()

    private val filter = MutableStateFlow("")

    private val displayedNotes: Flow<List<Note>> = combine(allNotes, filter) { cards, filter ->
        cards.filter { card ->
            card.english.contains(filter, true)
                    || card.japanese.contains(filter, true)
                    || card.reading.contains(filter, true)
        }
    }

    val myWordsUIState = displayedNotes.map { notes -> MyWordsUIState(notes.map { NoteUIState.fromNote(it) }) }

    val countText: StateFlow<String> = displayedNotes.map {
        "Word count: ${it.size}"
    }.stateIn(viewModelScope, SharingStarted.Lazily, "")


    fun filterWords(query: String) {
        filter.value = query
    }

    fun deleteWords(ids: Set<Int>) {
        viewModelScope.launch {
            deleteCardsWithIndexesUseCase(ids)
        }
    }

    fun resetWords(ids: Set<Int>) {
        viewModelScope.launch {
            resetNoteProgressByIdUseCase(ids)
        }
    }
}