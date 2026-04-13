package com.personal.kakeibox.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.personal.kakeibox.data.entity.CommuteEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface CommuteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: CommuteEntry)

    @Delete
    suspend fun delete(entry: CommuteEntry)

    // All commute calculations history newest first
    @Query("SELECT * FROM commute_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<CommuteEntry>>

    // Latest entry — used to pre-fill the calculator
    @Query("SELECT * FROM commute_entries ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestEntry(): CommuteEntry?

    @Query("DELETE FROM commute_entries WHERE id = :id")
    suspend fun deleteById(id: Int)
}