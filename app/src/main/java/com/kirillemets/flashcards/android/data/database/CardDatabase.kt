package com.kirillemets.flashcards.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kirillemets.flashcards.android.data.model.FlashCard

@Database(entities = [FlashCard::class], version = 1)
abstract class CardDatabase: RoomDatabase() {
    abstract fun flashCardsDao(): CardDatabaseDao
}