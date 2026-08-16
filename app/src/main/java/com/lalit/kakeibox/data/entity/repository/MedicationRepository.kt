package com.personal.kakeibox.data.entity.repository

import com.personal.kakeibox.data.dao.MedicationDao
import com.personal.kakeibox.data.entity.MedicationEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationRepository @Inject constructor(
    private val medicationDao: MedicationDao
) {
    fun getLogByDate(date: String): Flow<MedicationEntry?> = medicationDao.getLogByDate(date)

    suspend fun getLogByDateOnce(date: String): MedicationEntry? = medicationDao.getLogByDateOnce(date)

    fun getAllLogs(): Flow<List<MedicationEntry>> = medicationDao.getAllLogs()

    fun getFullyCompletedDaysCount(): Flow<Int> = medicationDao.getFullyCompletedDaysCount()

    suspend fun insertOrUpdate(entry: MedicationEntry) {
        medicationDao.insertOrUpdate(entry)
    }

    suspend fun toggleDose(date: String, doseType: String) {
        val existing = medicationDao.getLogByDateOnce(date) ?: MedicationEntry(date = date)
        val updated = when (doseType.lowercase()) {
            "breakfast" -> existing.copy(breakfastTaken = !existing.breakfastTaken)
            "lunch" -> existing.copy(lunchTaken = !existing.lunchTaken)
            "dinner" -> existing.copy(dinnerTaken = !existing.dinnerTaken)
            else -> existing
        }
        medicationDao.insertOrUpdate(updated)
    }
}
