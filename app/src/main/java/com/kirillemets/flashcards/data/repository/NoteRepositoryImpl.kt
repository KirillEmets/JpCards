package com.kirillemets.flashcards.data.repository

import com.kirillemets.flashcards.data.model.toNote
import com.kirillemets.flashcards.data.model.toNotes
import com.kirillemets.flashcards.domain.model.Note
import com.kirillemets.flashcards.domain.repository.NoteRepository
import com.kirillemets.flashcards.model.database.CardDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val db: CardDatabaseDao,
) : NoteRepository {
    override suspend fun get(id: Int): Note? = withContext(Dispatchers.IO) {
        db.get(id)?.toNote()
    }

    override suspend fun getNotesWithRelevantCards(currentTime: Long): List<Note> =
        withContext(Dispatchers.IO) {
            db.getRelevantCards(currentTime).toNotes()
        }

    override suspend fun updateRegularDelayAndTime(id: Int, delay: Int, time: Long) =
        withContext(Dispatchers.IO) {
            db.updateRegularDelayAndTime(id, delay, time)
        }

    override suspend fun updateReversedDelayAndTime(id: Int, delay: Int, time: Long) =
        withContext(Dispatchers.IO) {
            db.updateReversedDelayAndTime(id, delay, time)
        }

    override suspend fun deleteByIndexes(ids: Set<Int>) = withContext(Dispatchers.IO) {
        db.deleteByIndexes(ids)
    }
}