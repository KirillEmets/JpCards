package com.kirillemets.flashcards.database

import androidx.room.*

@Dao
interface CardDatabaseDao {
    @Query("SELECT * FROM flashcard")
    fun getAll(): List<FlashCard>

    @Insert
    fun insert(card: FlashCard)

    @Update
    fun update(card: FlashCard)

    @Delete
    fun delete(card: FlashCard)
}