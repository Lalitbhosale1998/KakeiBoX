package com.personal.kakeibox.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.personal.kakeibox.data.entity.RemittanceEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface RemittanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: RemittanceEntry)

    @Update
    suspend fun update(entry: RemittanceEntry)

    @Delete
    suspend fun delete(entry: RemittanceEntry)

    @Query("SELECT * FROM remittance_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<RemittanceEntry>>

    @Query("SELECT * FROM remittance_entries WHERE month = :month AND year = :year ORDER BY createdAt DESC")
    fun getEntriesByMonthYear(month: Int, year: Int): Flow<List<RemittanceEntry>>

    @Query("SELECT SUM(amount) FROM remittance_entries WHERE month = :month AND year = :year")
    fun getTotalByMonthYear(month: Int, year: Int): Flow<Long?>

    @Query("SELECT SUM(amount) FROM remittance_entries")
    fun getTotalAllTime(): Flow<Long?>
}
