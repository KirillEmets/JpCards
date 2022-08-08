package com.kirillemets.flashcards.android.domain.repository

import com.kirillemets.flashcards.android.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAll(): Flow<List<Note>>

    suspend fun get(id: Int): Note?

    suspend fun getNotesWithRelevantCards(currentTime: Long): List<Note>

    suspend fun updateRegularDelayAndTime(id: Int, delay: Int, time: Long)
    suspend fun updateReversedDelayAndTime(id: Int, delay: Int, time: Long)

    suspend fun resetDelayByIds(ids: Set<Int>)
    suspend fun resetDelayByIdsReversed(ids: Set<Int>)

    suspend fun deleteByIndexes(ids: Set<Int>)
    suspend fun deleteAll()

    suspend fun insertNew(note: Note): Boolean
    suspend fun insertNew(notes: List<Note>)
}