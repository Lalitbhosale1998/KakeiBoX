package com.personal.kakeibox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_entries")
data class ExerciseEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val sets: Int,
    val reps: Int,
    val description: String,
    val dayOfWeek: String, // e.g. "Monday", "Tuesday", etc.
    val createdAt: Long = System.currentTimeMillis()
)
