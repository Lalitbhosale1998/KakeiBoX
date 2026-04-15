package com.personal.kakeibox.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.personal.kakeibox.data.entity.SpendCategory
import com.personal.kakeibox.data.entity.SpendEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: SpendEntry)

    @Update
    suspend fun update(entry: SpendEntry)

    @Delete
    suspend fun delete(entry: SpendEntry)

    // Get all spend entries for a specific month/year
    @Query("""
        SELECT * FROM spend_entries 
        WHERE month = :month AND year = :year 
        ORDER BY createdAt DESC
    """)
    fun getEntriesByMonthYear(month: Int, year: Int): Flow<List<SpendEntry>>

    // Get only NEED entries for a month
    @Query("""
        SELECT * FROM spend_entries 
        WHERE month = :month AND year = :year AND category = :category
        ORDER BY createdAt DESC
    """)
    fun getEntriesByCategory(
        month: Int,
        year: Int,
        category: SpendCategory
    ): Flow<List<SpendEntry>>

    // Total amount for a month
    @Query("""
        SELECT SUM(amount) FROM spend_entries 
        WHERE month = :month AND year = :year
    """)
    fun getTotalByMonthYear(month: Int, year: Int): Flow<Long?>

    // Total by category for a month
    @Query("""
        SELECT SUM(amount) FROM spend_entries 
        WHERE month = :month AND year = :year AND category = :category
    """)
    fun getTotalByCategory(
        month: Int,
        year: Int,
        category: SpendCategory
    ): Flow<Long?>

    // Total amount ever
    @Query("SELECT SUM(amount) FROM spend_entries")
    fun getTotalSpendAllTime(): Flow<Long?>

    // All entries ever — for history
    @Query("SELECT * FROM spend_entries ORDER BY year DESC, month DESC, createdAt DESC")
    fun getAllEntries(): Flow<List<SpendEntry>>

    @Query("DELETE FROM spend_entries WHERE id = :id")
    suspend fun deleteById(id: Int)
}