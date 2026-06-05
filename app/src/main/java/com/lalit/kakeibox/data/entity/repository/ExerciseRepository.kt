package com.personal.kakeibox.data.repository

import com.personal.kakeibox.data.dao.ExerciseDao
import com.personal.kakeibox.data.entity.ExerciseEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepository @Inject constructor(
    private val exerciseDao: ExerciseDao
) {
    fun getExercisesForDay(day: String): Flow<List<ExerciseEntry>> =
        exerciseDao.getExercisesForDay(day)

    fun getAllExercises(): Flow<List<ExerciseEntry>> =
        exerciseDao.getAllExercises()

    suspend fun insert(entry: ExerciseEntry) =
        exerciseDao.insert(entry)

    suspend fun update(entry: ExerciseEntry) =
        exerciseDao.update(entry)

    suspend fun delete(entry: ExerciseEntry) =
        exerciseDao.delete(entry)

    suspend fun deleteById(id: Int) =
        exerciseDao.deleteById(id)
}
