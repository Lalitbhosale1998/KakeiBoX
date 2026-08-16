package com.personal.kakeibox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_logs")
data class MedicationEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // Format: "yyyy-MM-dd"
    val breakfastTaken: Boolean = false,
    val lunchTaken: Boolean = false,
    val dinnerTaken: Boolean = false
)
