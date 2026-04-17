package com.personal.kakeibox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "birthdays")
data class BirthdayEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val date: LocalDate,
    val isEnabled: Boolean = true
)