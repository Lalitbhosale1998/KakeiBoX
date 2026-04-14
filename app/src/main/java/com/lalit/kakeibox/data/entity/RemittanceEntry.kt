package com.personal.kakeibox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remittance_entries")
data class RemittanceEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val amount: Long,
    val note: String = "",
    val month: Int,
    val year: Int,
    
    val createdAt: Long = System.currentTimeMillis()
)
