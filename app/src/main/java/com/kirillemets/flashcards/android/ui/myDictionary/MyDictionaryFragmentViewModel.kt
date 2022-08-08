package com.kirillemets.flashcards.android.ui.myDictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirillemets.flashcards.android.domain.model.Note
import com.kirillemets.flashcards.android.domain.usecase.DeleteCardsWithIndexesUseCase
import com.kirillemets.flashcards.android.domain.usecase.GetAllCardsUseCase
import com.kirillemets.flashcards.android.domain.usecase.ResetNoteProgressByIdUseCase
import com.kirillemets.flashcards.android.domain.usecase.SpeakTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyWordsUIState(
    val loading: Boolean = true,
    val dictionaryEmpty: Boolean = false,
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


    private val allNotes: StateFlow<List<Note>?> =
        getAllCardsUseCase().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val filter = MutableStateFlow("")
    private val ttsFailed = MutableStateFlow(false)
    private val selectedNotesIds = MutableStateFlow(setOf<Int>())
    private val openedNotesIds = MutableStateFlow(setOf<Int>())

    val myWordsUIState = combine(
        allNotes,
        ttsFailed,
        filter,
        selectedNotesIds,
        openedNotesIds
    ) { allNotes, ttsFailed, filter, selectedNotesIds, openedNotesIds ->
        MyWordsUIState(
            allNotes == null,
            allNotes?.isEmpty() ?: false,
            allNotes?.filterVisible(filter)?.map {
                NoteUIState.fromNote(
                    it,
                    selectedNotesIds.contains(it.noteId),
                    openedNotesIds.contains(it.noteId)
                )
            } ?: emptyList(),
            ttsFailed,
            filter,
        )
    }

    private fun List<Note>.filterVisible(filter: String) = filter { card ->
        card.english.contains(filter, true)
                || card.japanese.contains(filter, true)
                || card.reading.contains(filter, true)
    }

    fun onDeleteSelectedWords() {
        viewModelScope.launch {
            deleteCardsWithIndexesUseCase(selectedNotesIds.value)
            selectedNotesIds.value = emptySet()
        }
    }

    fun onResetSelectedWords() {
        viewModelScope.launch {
            resetNoteProgressByIdUseCase(selectedNotesIds.value)
            selectedNotesIds.value = emptySet()
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
        allNotes.value?.find { it.noteId == id }?.let {
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