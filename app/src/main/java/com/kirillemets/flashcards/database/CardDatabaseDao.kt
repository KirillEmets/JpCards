package com.kirillemets.flashcards.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDatabaseDao {
    @Query("SELECT * FROM flashcard")
    fun getAll(): LiveData<List<FlashCard>>

    @Insert
    fun insert(card: FlashCard)

    @Update
    fun update(card: FlashCard)

    @Delete
    fun delete(card: FlashCard)
}