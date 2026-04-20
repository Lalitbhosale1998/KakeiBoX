package com.personal.kakeibox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "salary_entries")
data class SalaryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Month 1-12, Year e.g. 2026
    val month: Int,
    val year: Int,

    // All amounts in whole numbers
    val salaryAmount: Long,
    val remittanceAmount: Long,
    val savingsAmount: Long,

    // Calculated automatically = salary - remittance - savings
    val remainingAmount: Long,

    // Optional note
    val note: String = "",

    // Timestamp for sorting
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)