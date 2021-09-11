package com.kirillemets.flashcards.database

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class FlashCardRepository(context: Context) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db = CardDatabase.getInstance(context).flashCardsDao()

    fun getAll(): LiveData<List<FlashCard>> = db.getAll()

    fun getAllBlocking(): List<FlashCard> = db.getAllBlocking()

    suspend fun get(id: Int): FlashCard? =
        withContext(coroutineScope.coroutineContext) { db.get(id) }

    suspend fun getRelevantCards(currentTime: Long): List<FlashCard> =
        withContext(coroutineScope.coroutineContext) { db.getRelevantCards(currentTime) }

    suspend fun find(english: String, japanese: String, reading: String): List<FlashCard> =
        withContext(coroutineScope.coroutineContext) { db.find(english, japanese, reading) }

    fun deleteByIndexes(ids: Set<Int>) =
        coroutineScope.launch { db.deleteByIndexes(ids) }

    fun resetDelayByIds(ids: Set<Int>, time: Long) =
        coroutineScope.launch { db.resetDelayByIds(ids, time) }

    fun resetDelayByIdsReversed(ids: Set<Int>, time: Long) =
        coroutineScope.launch { db.resetDelayByIdsReversed(ids, time) }

    fun updateRegularDelayAndTime(id: Int, delay: Int, time: Long) =
        coroutineScope.launch { db.updateRegularDelayAndTime(id, delay, time) }

    fun updateReversedDelayAndTime(id: Int, delay: Int, time: Long) =
        coroutineScope.launch { db.updateReversedDelayAndTime(id, delay, time) }

    suspend fun insertNew(flashCard: FlashCard): Boolean =
        withContext(coroutineScope.coroutineContext) {
            if (find(flashCard.english, flashCard.japanese, flashCard.reading).isNotEmpty()) {
                return@withContext false
            }
            insert(flashCard)
            return@withContext true
        }

    fun insert(card: FlashCard) =
        coroutineScope.launch { db.insert(card) }

    fun insert(cards: List<FlashCard>) =
        coroutineScope.launch { db.insert(cards) }

    fun deleteAll() =
        coroutineScope.launch { db.deleteAll() }
}