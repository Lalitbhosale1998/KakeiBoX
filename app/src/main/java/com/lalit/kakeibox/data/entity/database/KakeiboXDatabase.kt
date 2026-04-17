package com.personal.kakeibox.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.personal.kakeibox.data.converter.Converters
import com.personal.kakeibox.data.dao.BirthdayDao
import com.personal.kakeibox.data.dao.CommuteDao
import com.personal.kakeibox.data.dao.SalaryDao
import com.personal.kakeibox.data.dao.SpendDao
import com.personal.kakeibox.data.entity.BirthdayEntry
import com.personal.kakeibox.data.entity.CommuteEntry
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.entity.SpendEntry

@Database(
    entities = [
        SalaryEntry::class,
        SpendEntry::class,
        CommuteEntry::class,
        BirthdayEntry::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KakeiboXDatabase : RoomDatabase() {
    abstract fun salaryDao(): SalaryDao
    abstract fun spendDao(): SpendDao
    abstract fun commuteDao(): CommuteDao
    abstract fun birthdayDao(): BirthdayDao
}