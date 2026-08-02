package com.personal.kakeibox.data.dao

import androidx.room.*
import com.personal.kakeibox.data.entity.VocabEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabDao {
    @Query("SELECT * FROM vocab_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<VocabEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: VocabEntry)

    @Update
    suspend fun updateEntry(entry: VocabEntry)

    @Delete
    suspend fun deleteEntry(entry: VocabEntry)

    @Query("SELECT COUNT(*) FROM vocab_entries")
    suspend fun getEntryCount(): Int

    @Query("DELETE FROM vocab_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Int)
}
