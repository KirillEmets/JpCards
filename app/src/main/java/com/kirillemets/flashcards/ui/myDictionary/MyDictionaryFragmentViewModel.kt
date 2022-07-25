package com.kirillemets.flashcards.ui.myDictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirillemets.flashcards.domain.model.Note
import com.kirillemets.flashcards.domain.usecase.DeleteCardsWithIndexesUseCase
import com.kirillemets.flashcards.domain.usecase.GetAllCardsUseCase
import com.kirillemets.flashcards.domain.usecase.ResetNoteProgressByIdUseCase
import com.kirillemets.flashcards.domain.usecase.SpeakTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyWordsUIState(
    val notes: List<NoteUIState> = emptyList(),
    val ttsFailed: Boolean = false,
    val filter: String = "",
) {
    val wordCount: Int
        get() = notes.size
}

@HiltViewModel
class MyDictionaryFragmentViewModel @Inject constructor(
    getAllCardsUseCase: GetAllCardsUseCase,
    private val deleteCardsWithIndexesUseCase: DeleteCardsWithIndexesUseCase,
    private val resetNoteProgressByIdUseCase: ResetNoteProgressByIdUseCase,
    private val speakTextUseCase: SpeakTextUseCase,
) : ViewModel() {


    private val allNotes: Flow<List<Note>> = getAllCardsUseCase()
    private val filter = MutableStateFlow("")
    private val ttsFailed = MutableStateFlow(false)
    private val selectedNotesIds = MutableStateFlow(setOf<Int>())
    private val openedNotesIds = MutableStateFlow(setOf<Int>())

    private val displayedNotes: StateFlow<List<Note>> = combine(allNotes, filter) { cards, filter ->
        cards.filter { card ->
            card.english.contains(filter, true)
                    || card.japanese.contains(filter, true)
                    || card.reading.contains(filter, true)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    val myWordsUIState = combine(
        displayedNotes,
        ttsFailed,
        filter,
        selectedNotesIds,
        openedNotesIds
    ) { displayedNotes, ttsFailed, filter, selectedNotesIds, openedNotesIds ->
        MyWordsUIState(
            displayedNotes.map {
                NoteUIState.fromNote(
                    it,
                    selectedNotesIds.contains(it.noteId),
                    openedNotesIds.contains(it.noteId)
                )
            },
            ttsFailed,
            filter,
        )
    }

    val countText: StateFlow<String> = displayedNotes.map {
        "Word count: ${it.size}"
    }.stateIn(viewModelScope, SharingStarted.Lazily, "")

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

    fun onItemClick(noteId: Int) {
        if (selectedNotesIds.value.isNotEmpty()) {
            selectedNotesIds.update {
                if (it.contains(noteId))
                    it - noteId
                else it + noteId
            }
            return
        }

        openedNotesIds.update {
            if (it.contains(noteId))
                it - noteId
            else it + noteId
        }
    }

    fun onItemLongClick(noteId: Int) {
        selectedNotesIds.update {
            if (it.contains(noteId))
                it - noteId
            else it + noteId
        }
    }

    fun resetTtsFailed() {
        ttsFailed.value = false
    }

    fun onTtsClick(id: Int) {
        displayedNotes.value.find { it.noteId == id }?.let {
            val textToSpeak = if (it.japanese.length >= 2) it.japanese else it.reading
            val ttsSucceeded = speakTextUseCase(textToSpeak)
            if (!ttsSucceeded)
                ttsFailed.value = true
        }
    }

    fun onFilterValueChange(value: String) {
        filter.value = value
    }
}