package com.kirillemets.flashcards.domain.repository

import com.kirillemets.flashcards.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
//    fun getAll(): Flow<List<Note>>

    suspend fun get(id: Int): Note?

    suspend fun getNotesWithRelevantCards(currentTime: Long): List<Note>


    suspend fun updateRegularDelayAndTime(id: Int, delay: Int, time: Long)
    suspend fun updateReversedDelayAndTime(id: Int, delay: Int, time: Long)

    suspend fun deleteByIndexes(ids: Set<Int>)

//    suspend fun find(english: String, japanese: String, reading: String): List<Note>
//
//    fun resetDelayByIds(ids: Set<Int>, time: Long)
//
//    fun resetDelayByIdsReversed(ids: Set<Int>, time: Long)
//
//
//
//    suspend fun insertNew(note: Note): Boolean
//
//    fun insert(note: Note)
//
//    fun insert(notes: List<Note>)
//
//    suspend fun insertSuspend(note: Note)
//
//    fun deleteAll()
}