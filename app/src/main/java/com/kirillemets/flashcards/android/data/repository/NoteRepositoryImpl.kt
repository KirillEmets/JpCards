package com.kirillemets.flashcards.android.data.repository

import com.kirillemets.flashcards.android.data.model.toNote
import com.kirillemets.flashcards.android.data.model.toNotes
import com.kirillemets.flashcards.shared.domain.model.Note
import com.kirillemets.flashcards.shared.domain.repository.NoteRepository
import com.kirillemets.flashcards.android.data.database.CardDatabaseDao
import com.kirillemets.flashcards.android.data.model.toFlashCard
import com.kirillemets.flashcards.android.data.model.toFlashCards
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val db: CardDatabaseDao,
) : NoteRepository {
    override fun getAll(): Flow<List<Note>> = db.getAll().map { it.toNotes() }

    override suspend fun get(id: Int): Note? =
        db.get(id)?.toNote()


    override suspend fun getNotesWithRelevantCards(currentTime: Long): List<Note> =
        db.getRelevantCards(currentTime).toNotes()


    override suspend fun updateRegularDelayAndTime(id: Int, delay: Int, time: Long) =
        db.updateRegularDelayAndTime(id, delay, time)


    override suspend fun updateReversedDelayAndTime(id: Int, delay: Int, time: Long) =
        db.updateReversedDelayAndTime(id, delay, time)


    override suspend fun resetDelayByIds(ids: Set<Int>) = db.resetDelayByIds(ids, 0)

    override suspend fun resetDelayByIdsReversed(ids: Set<Int>) = db.resetDelayByIdsReversed(ids, 0)

    override suspend fun deleteByIndexes(ids: Set<Int>) =
        db.deleteByIndexes(ids)

    override suspend fun insertNew(note: Note): Boolean {
        if (db.find(note.english, note.japanese, note.reading).isNotEmpty()) {
            return false
        }
        db.insert(note.toFlashCard())
        return true
    }

    override suspend fun insertNew(notes: List<Note>) {
        val hashesInDb =
            db.getAllSuspend().map { (it.english + it.japanese + it.reading).hashCode() }

        val toInsert =
            notes.filterNot { hashesInDb.contains((it.english + it.japanese + it.reading).hashCode()) }

        db.insert(toInsert.toFlashCards())
    }

    override suspend fun deleteAll() {
        db.deleteAll()
    }
}