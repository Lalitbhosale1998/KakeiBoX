package com.personal.kakeibox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Category type — Need or Want
enum class SpendCategory {
    NEED, WANT
}

@Entity(tableName = "spend_entries")
data class SpendEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val description: String,
    val amount: Long,
    val category: SpendCategory,

    // Which month/year this spend belongs to
    val month: Int,
    val year: Int,

    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)