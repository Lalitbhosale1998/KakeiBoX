package com.personal.kakeibox.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.personal.kakeibox.data.entity.MedicationEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: MedicationEntry)

    @Update
    suspend fun update(entry: MedicationEntry)

    @Query("SELECT * FROM medication_logs WHERE date = :date LIMIT 1")
    fun getLogByDate(date: String): Flow<MedicationEntry?>

    @Query("SELECT * FROM medication_logs WHERE date = :date LIMIT 1")
    suspend fun getLogByDateOnce(date: String): MedicationEntry?

    @Query("SELECT * FROM medication_logs ORDER BY date ASC")
    fun getAllLogs(): Flow<List<MedicationEntry>>

    @Query("SELECT COUNT(*) FROM medication_logs WHERE breakfastTaken = 1 AND lunchTaken = 1 AND dinnerTaken = 1")
    fun getFullyCompletedDaysCount(): Flow<Int>
}
