package com.personal.kakeibox.data.repository

import com.personal.kakeibox.data.dao.SpendDao
import com.personal.kakeibox.data.entity.SpendCategory
import com.personal.kakeibox.data.entity.SpendEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpendRepository @Inject constructor(
    private val spendDao: SpendDao
) {
    fun getEntriesByMonthYear(month: Int, year: Int): Flow<List<SpendEntry>> =
        spendDao.getEntriesByMonthYear(month, year)

    fun getEntriesByCategory(
        month: Int,
        year: Int,
        category: SpendCategory
    ): Flow<List<SpendEntry>> =
        spendDao.getEntriesByCategory(month, year, category)

    fun getTotalByMonthYear(month: Int, year: Int): Flow<Long?> =
        spendDao.getTotalByMonthYear(month, year)

    fun getTotalByCategory(
        month: Int,
        year: Int,
        category: SpendCategory
    ): Flow<Long?> =
        spendDao.getTotalByCategory(month, year, category)

    fun getTotalSpendAllTime(): Flow<Long?> =
        spendDao.getTotalSpendAllTime()

    fun getAllEntries(): Flow<List<SpendEntry>> =
        spendDao.getAllEntries()

    suspend fun insert(entry: SpendEntry) =
        spendDao.insert(entry)

    suspend fun update(entry: SpendEntry) =
        spendDao.update(entry)

    suspend fun delete(entry: SpendEntry) =
        spendDao.delete(entry)

    suspend fun deleteById(id: Int) =
        spendDao.deleteById(id)
}