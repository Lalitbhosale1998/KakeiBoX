package com.personal.kakeibox.data.dao

import androidx.room.*
import com.personal.kakeibox.data.entity.BirthdayEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface BirthdayDao {
    @Query("SELECT * FROM birthdays ORDER BY date ASC")
    fun getAllBirthdays(): Flow<List<BirthdayEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBirthday(birthday: BirthdayEntry)

    @Delete
    suspend fun deleteBirthday(birthday: BirthdayEntry)

    @Update
    suspend fun updateBirthday(birthday: BirthdayEntry)
}