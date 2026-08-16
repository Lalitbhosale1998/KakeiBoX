package com.personal.kakeibox.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.personal.kakeibox.data.converter.Converters
import com.personal.kakeibox.data.dao.BirthdayDao
import com.personal.kakeibox.data.dao.CommuteDao
import com.personal.kakeibox.data.dao.ExerciseDao
import com.personal.kakeibox.data.dao.MedicationDao
import com.personal.kakeibox.data.dao.SalaryDao
import com.personal.kakeibox.data.dao.SpendDao
import com.personal.kakeibox.data.dao.VocabDao
import com.personal.kakeibox.data.entity.BirthdayEntry
import com.personal.kakeibox.data.entity.CommuteEntry
import com.personal.kakeibox.data.entity.ExerciseEntry
import com.personal.kakeibox.data.entity.MedicationEntry
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.entity.SpendEntry
import com.personal.kakeibox.data.entity.VocabEntry

@Database(
    entities = [
        SalaryEntry::class,
        SpendEntry::class,
        CommuteEntry::class,
        BirthdayEntry::class,
        ExerciseEntry::class,
        VocabEntry::class,
        MedicationEntry::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KakeiboXDatabase : RoomDatabase() {
    abstract fun salaryDao(): SalaryDao
    abstract fun spendDao(): SpendDao
    abstract fun commuteDao(): CommuteDao
    abstract fun birthdayDao(): BirthdayDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun vocabDao(): VocabDao
    abstract fun medicationDao(): MedicationDao
}