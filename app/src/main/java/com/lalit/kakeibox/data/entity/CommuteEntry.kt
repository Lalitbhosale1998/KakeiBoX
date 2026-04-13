package com.personal.kakeibox.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "commute_entries")
data class CommuteEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // One-way fare in yen
    val oneWayFare: Long,

    // Number of holidays in the period
    val holidays: Int,

    // Number of WFH days in the period
    val wfhDays: Int,

    // Period: from this day to 25th of next month
    // Store as epoch milliseconds
    val periodStartDate: Long,
    val periodEndDate: Long,

    // Calculated results stored for history
    val totalWorkingDays: Int,
    val totalCommuteDays: Int,
    val totalCost: Long,

    val createdAt: Long = System.currentTimeMillis()
)