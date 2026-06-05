package com.personal.kakeibox.data.dao

import androidx.room.*
import com.personal.kakeibox.data.entity.ExerciseEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: ExerciseEntry)

    @Update
    suspend fun update(entry: ExerciseEntry)

    @Delete
    suspend fun delete(entry: ExerciseEntry)

    @Query("SELECT * FROM exercise_entries WHERE dayOfWeek = :day ORDER BY createdAt ASC")
    fun getExercisesForDay(day: String): Flow<List<ExerciseEntry>>

    @Query("SELECT * FROM exercise_entries ORDER BY dayOfWeek, createdAt ASC")
    fun getAllExercises(): Flow<List<ExerciseEntry>>

    @Query("DELETE FROM exercise_entries WHERE id = :id")
    suspend fun deleteById(id: Int)
}
