package com.personal.kakeibox.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.personal.kakeibox.data.entity.SalaryEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface SalaryDao {

    // Insert new entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: SalaryEntry)

    // Update existing entry
    @Update
    suspend fun update(entry: SalaryEntry)

    // Delete entry
    @Delete
    suspend fun delete(entry: SalaryEntry)

    // Get all entries ordered by year and month descending (newest first)
    @Query("SELECT * FROM salary_entries ORDER BY year DESC, month DESC")
    fun getAllEntries(): Flow<List<SalaryEntry>>

    // Get entry for a specific month and year
    @Query("SELECT * FROM salary_entries WHERE month = :month AND year = :year LIMIT 1")
    fun getEntryByMonthYear(month: Int, year: Int): Flow<SalaryEntry?>

    // Get current month entry
    @Query("""
        SELECT * FROM salary_entries 
        WHERE month = :month AND year = :year 
        LIMIT 1
    """)
    suspend fun getEntryByMonthYearOnce(month: Int, year: Int): SalaryEntry?

    // Delete by id
    @Query("DELETE FROM salary_entries WHERE id = :id")
    suspend fun deleteById(id: Int)

    // Get total savings across all months
    @Query("SELECT SUM(savingsAmount) FROM salary_entries")
    fun getTotalSavings(): Flow<Long?>

    // Get entries for a specific year
    @Query("SELECT * FROM salary_entries WHERE year = :year ORDER BY month DESC")
    fun getEntriesByYear(year: Int): Flow<List<SalaryEntry>>
}