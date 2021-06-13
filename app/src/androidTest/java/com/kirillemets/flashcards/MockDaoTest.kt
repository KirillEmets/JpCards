package com.kirillemets.flashcards

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kirillemets.flashcards.database.CardDatabase
import com.kirillemets.flashcards.database.CardDatabaseDao
import org.junit.Before

open class MockDaoTest {
    internal lateinit var dao: CardDatabaseDao

    @Before
    fun mockDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, CardDatabase::class.java).build()
        CardDatabase.setInstance(db)
        dao = db.flashCardsDao()
    }
}