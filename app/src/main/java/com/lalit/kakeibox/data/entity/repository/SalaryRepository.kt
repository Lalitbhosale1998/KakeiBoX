package com.personal.kakeibox.data.repository

import com.personal.kakeibox.data.dao.SalaryDao
import com.personal.kakeibox.data.entity.SalaryEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SalaryRepository @Inject constructor(
    private val salaryDao: SalaryDao
) {
    fun getAllEntries(): Flow<List<SalaryEntry>> =
        salaryDao.getAllEntries()

    fun getEntryByMonthYear(month: Int, year: Int): Flow<SalaryEntry?> =
        salaryDao.getEntryByMonthYear(month, year)

    fun getTotalSavings(): Flow<Long?> =
        salaryDao.getTotalSavings()

    fun getTotalSalary(): Flow<Long?> =
        salaryDao.getTotalSalary()

    fun getTotalRemittance(): Flow<Long?> =
        salaryDao.getTotalRemittance()

    fun getEntriesByYear(year: Int): Flow<List<SalaryEntry>> =
        salaryDao.getEntriesByYear(year)

    suspend fun getEntryByMonthYearOnce(month: Int, year: Int): SalaryEntry? =
        salaryDao.getEntryByMonthYearOnce(month, year)

    suspend fun insert(entry: SalaryEntry) =
        salaryDao.insert(entry)

    suspend fun update(entry: SalaryEntry) =
        salaryDao.update(entry)

    suspend fun delete(entry: SalaryEntry) =
        salaryDao.delete(entry)

    suspend fun deleteById(id: Int) =
        salaryDao.deleteById(id)
}