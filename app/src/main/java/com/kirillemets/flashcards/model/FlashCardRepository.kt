package com.kirillemets.flashcards.model

import com.kirillemets.flashcards.domain.model.DictionaryEntry
import com.kirillemets.flashcards.data.database.CardDatabaseDao
import com.kirillemets.flashcards.data.apiService.JishoApiService
import com.kirillemets.flashcards.data.model.FlashCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class FlashCardRepository @Inject constructor(
    private val db: CardDatabaseDao,
    private val jisho: JishoApiService
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getAll(): Flow<List<FlashCard>> = db.getAll()

    suspend fun getAllSuspend(): List<FlashCard> = withContext(coroutineScope.coroutineContext) { db.getAllBlocking() }

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

    suspend fun insertNew(noteEntity: FlashCard): Boolean =
        withContext(coroutineScope.coroutineContext) {
            if (find(noteEntity.english, noteEntity.japanese, noteEntity.reading).isNotEmpty()) {
                return@withContext false
            }
            insertSuspend(noteEntity)
            return@withContext true
        }

    fun insert(card: FlashCard) =
        coroutineScope.launch { db.insert(card) }

    suspend fun insertSuspend(card: FlashCard) =
        withContext(coroutineScope.coroutineContext) { db.insert(card) }

    fun insert(cards: List<FlashCard>) =
        coroutineScope.launch { db.insert(cards) }

    fun deleteAll() =
        coroutineScope.launch { db.deleteAll() }

    suspend fun searchWordsJisho(query: String): List<DictionaryEntry> =
        withContext(coroutineScope.coroutineContext) {
            jisho.getQueryData(query).toDictionaryEntries()
        }

}