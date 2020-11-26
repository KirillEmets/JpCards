package com.kirillemets.flashcards.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDatabaseDao {
    @Query("SELECT * FROM flashcard ORDER BY cardId DESC")
    fun getAll(): LiveData<List<FlashCard>>

    @Query("SELECT * FROM flashcard WHERE cardId = :id")
    fun get(id: Int): FlashCard?

    @Query("SELECT * FROM flashcard WHERE next_review_time <= :currentTime OR next_review_time_reversed <= :currentTime")
    fun getRelevantCards(currentTime: Long) : List<FlashCard>

    @Query("DELETE FROM flashcard WHERE cardId IN (:ids)")
    fun deleteByIndexes(ids: Set<Int>)

    @Query("UPDATE flashcard SET last_delay = 1, next_review_time = :time WHERE cardId IN (:ids)")
    fun resetDelayByIds(ids: Set<Int>, time: Long)

    @Query("UPDATE flashcard SET last_delay_reversed = 1, next_review_time_reversed = :time WHERE cardId IN (:ids)")
    fun resetDelayByIdsReversed(ids: Set<Int>, time: Long)

    @Query("UPDATE flashcard SET last_delay = :delay, next_review_time = :time WHERE cardId = :id")
    fun updateRegularDelayAndTime(id: Int, delay: Int, time: Long)

    @Query("UPDATE flashcard SET last_delay_reversed = :delay, next_review_time_reversed = :time WHERE cardId = :id")
    fun updateReversedDelayAndTime(id: Int, delay: Int, time: Long)

    @Insert
    fun insert(card: FlashCard)

    @Update
    fun update(card: FlashCard)

    @Delete
    fun delete(card: FlashCard)
}