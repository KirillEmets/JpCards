package com.kirillemets.flashcards.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

open class FlashCardRepository(
    context: Context
) {
    private val db = CardDatabase.getInstance(context).flashCardsDao()

    fun getAll(): LiveData<List<FlashCard>> = db.getAll()

    fun getAllBlocking(): List<FlashCard> = db.getAllBlocking()

    fun get(id: Int): FlashCard? = db.get(id)

    fun getRelevantCards(currentTime: Long): List<FlashCard> = db.getRelevantCards(currentTime)

    fun find(english: String, japanese: String, reading: String): List<FlashCard> =
        db.find(english, japanese, reading)

    fun deleteByIndexes(ids: Set<Int>) = db.deleteByIndexes(ids)

    fun resetDelayByIds(ids: Set<Int>, time: Long) = db.resetDelayByIds(ids, time)

    fun resetDelayByIdsReversed(ids: Set<Int>, time: Long) = db.resetDelayByIdsReversed(ids, time)

    fun updateRegularDelayAndTime(id: Int, delay: Int, time: Long) =
        db.updateRegularDelayAndTime(id, delay, time)

    fun updateReversedDelayAndTime(id: Int, delay: Int, time: Long) =
        db.updateReversedDelayAndTime(id, delay, time)

    fun insert(card: FlashCard) = db.insert(card)

    fun update(card: FlashCard) = db.update(card)

    fun delete(card: FlashCard) = db.delete(card)
}