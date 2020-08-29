package com.kirillemets.flashcards.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FlashCard(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val japanese: String,
    val reading: String,
    val english: String,
)