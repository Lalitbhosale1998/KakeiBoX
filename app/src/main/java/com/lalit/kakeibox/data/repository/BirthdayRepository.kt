package com.personal.kakeibox.data.repository

import com.personal.kakeibox.data.dao.BirthdayDao
import com.personal.kakeibox.data.entity.BirthdayEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BirthdayRepository @Inject constructor(
    private val birthdayDao: BirthdayDao
) {
    fun getAllBirthdays(): Flow<List<BirthdayEntry>> = birthdayDao.getAllBirthdays()

    suspend fun insertBirthday(birthday: BirthdayEntry) = birthdayDao.insertBirthday(birthday)

    suspend fun deleteBirthday(birthday: BirthdayEntry) = birthdayDao.deleteBirthday(birthday)

    suspend fun updateBirthday(birthday: BirthdayEntry) = birthdayDao.updateBirthday(birthday)
}