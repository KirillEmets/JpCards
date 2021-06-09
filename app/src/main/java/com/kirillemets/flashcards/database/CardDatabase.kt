package com.kirillemets.flashcards.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.jetbrains.annotations.TestOnly

@Database(entities = [FlashCard::class], version = 1)
abstract class CardDatabase: RoomDatabase() {
    companion object {
        private var Instance: CardDatabase? = null
        fun getInstance(context: Context): CardDatabase {
            synchronized(this) {
                var instance = Instance

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        CardDatabase::class.java,
                        "flashcards_database"
                    )
                        .build()
                }
                return instance
            }
        }

        @TestOnly
        fun setInstance(database: CardDatabase) {
            Instance = database
        }
    }
    abstract fun flashCardsDao(): CardDatabaseDao
}