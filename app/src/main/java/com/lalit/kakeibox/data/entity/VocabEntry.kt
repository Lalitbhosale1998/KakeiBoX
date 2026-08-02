package com.personal.kakeibox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocab_entries")
data class VocabEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val kanjiWord: String,
    val furiganaReading: String,
    val meaning: String,
    val category: String,
    val subCategory: String = "",
    val studyTag: String,
    val exampleSentence: String = "",
    val isMastered: Boolean = false,
    val isStarred: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
