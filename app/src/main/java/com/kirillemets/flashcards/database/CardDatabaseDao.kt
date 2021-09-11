package com.kirillemets.flashcards.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDatabaseDao {
    @Query("SELECT * FROM flashcard ORDER BY cardId DESC")
    fun getAll(): LiveData<List<FlashCard>>

    @Query("SELECT * FROM flashcard ORDER BY cardId DESC")
    fun getAllBlocking(): List<FlashCard>

    @Query("SELECT * FROM flashcard WHERE cardId = :id")
    suspend fun get(id: Int): FlashCard?

    @Query("SELECT * FROM flashcard WHERE next_review_time <= :currentTime OR next_review_time_reversed <= :currentTime")
    suspend fun getRelevantCards(currentTime: Long) : List<FlashCard>

    @Query("SELECT * FROM flashcard WHERE :english = english AND :japanese = japanese AND :reading = reading")
    suspend fun find(english: String, japanese: String, reading: String): List<FlashCard>

    @Query("DELETE FROM flashcard WHERE cardId IN (:ids)")
    suspend fun deleteByIndexes(ids: Set<Int>)

    @Query("UPDATE flashcard SET last_delay = 1, next_review_time = :time WHERE cardId IN (:ids)")
    suspend fun resetDelayByIds(ids: Set<Int>, time: Long)

    @Query("UPDATE flashcard SET last_delay_reversed = 1, next_review_time_reversed = :time WHERE cardId IN (:ids)")
    suspend fun resetDelayByIdsReversed(ids: Set<Int>, time: Long)

    @Query("UPDATE flashcard SET last_delay = :delay, next_review_time = :time WHERE cardId = :id")
    suspend fun updateRegularDelayAndTime(id: Int, delay: Int, time: Long)

    @Query("UPDATE flashcard SET last_delay_reversed = :delay, next_review_time_reversed = :time WHERE cardId = :id")
    suspend fun updateReversedDelayAndTime(id: Int, delay: Int, time: Long)

    @Query("DELETE FROM flashcard")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(card: FlashCard)

    @Insert
    suspend fun insert(cards: List<FlashCard>)

    @Update
    suspend fun update(card: FlashCard)

    @Delete
    suspend fun delete(card: FlashCard)
}