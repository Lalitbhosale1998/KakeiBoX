package com.personal.kakeibox.data.repository

import com.personal.kakeibox.data.dao.RemittanceDao
import com.personal.kakeibox.data.entity.RemittanceEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemittanceRepository @Inject constructor(
    private val remittanceDao: RemittanceDao
) {

    suspend fun insert(entry: RemittanceEntry) = remittanceDao.insert(entry)

    suspend fun update(entry: RemittanceEntry) = remittanceDao.update(entry)

    suspend fun delete(entry: RemittanceEntry) = remittanceDao.delete(entry)

    fun getAllEntries(): Flow<List<RemittanceEntry>> = remittanceDao.getAllEntries()

    fun getEntriesByMonthYear(month: Int, year: Int): Flow<List<RemittanceEntry>> =
        remittanceDao.getEntriesByMonthYear(month, year)

    fun getTotalByMonthYear(month: Int, year: Int): Flow<Long> =
        remittanceDao.getTotalByMonthYear(month, year).map { it ?: 0L }

    fun getTotalAllTime(): Flow<Long> =
        remittanceDao.getTotalAllTime().map { it ?: 0L }
}
