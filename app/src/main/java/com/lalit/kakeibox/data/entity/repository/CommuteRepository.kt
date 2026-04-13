package com.personal.kakeibox.data.repository

import com.personal.kakeibox.data.dao.CommuteDao
import com.personal.kakeibox.data.entity.CommuteEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommuteRepository @Inject constructor(
    private val commuteDao: CommuteDao
) {
    fun getAllEntries(): Flow<List<CommuteEntry>> =
        commuteDao.getAllEntries()

    suspend fun getLatestEntry(): CommuteEntry? =
        commuteDao.getLatestEntry()

    suspend fun insert(entry: CommuteEntry) =
        commuteDao.insert(entry)

    suspend fun delete(entry: CommuteEntry) =
        commuteDao.delete(entry)

    suspend fun deleteById(id: Int) =
        commuteDao.deleteById(id)
}