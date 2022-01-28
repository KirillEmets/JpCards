package com.kirillemets.flashcards.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kirillemets.flashcards.data.model.FlashCard
import org.jetbrains.annotations.TestOnly

@Database(entities = [FlashCard::class], version = 1)
abstract class CardDatabase: RoomDatabase() {
    companion object {
        private var Instance: CardDatabase? = null

        @TestOnly
        fun setInstance(database: CardDatabase) {
            Instance = database
        }
    }
    abstract fun flashCardsDao(): CardDatabaseDao
}