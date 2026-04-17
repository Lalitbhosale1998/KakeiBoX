package com.personal.kakeibox.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.personal.kakeibox.data.dao.BirthdayDao
import com.personal.kakeibox.data.dao.CommuteDao
import com.personal.kakeibox.data.dao.SalaryDao
import com.personal.kakeibox.data.dao.SpendDao
import com.personal.kakeibox.data.database.KakeiboXDatabase
import com.personal.kakeibox.data.repository.BirthdayRepository
import com.personal.kakeibox.data.repository.CommuteRepository
import com.personal.kakeibox.data.repository.SalaryRepository
import com.personal.kakeibox.data.repository.SpendRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile("user_preferences") }
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): KakeiboXDatabase {
        return Room.databaseBuilder(
            context,
            KakeiboXDatabase::class.java,
            "kakeibox_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSalaryDao(database: KakeiboXDatabase): SalaryDao =
        database.salaryDao()

    @Provides
    @Singleton
    fun provideSpendDao(database: KakeiboXDatabase): SpendDao =
        database.spendDao()

    @Provides
    @Singleton
    fun provideCommuteDao(database: KakeiboXDatabase): CommuteDao =
        database.commuteDao()

    @Provides
    @Singleton
    fun provideBirthdayDao(database: KakeiboXDatabase): BirthdayDao =
        database.birthdayDao()

    @Provides
    @Singleton
    fun provideSalaryRepository(salaryDao: SalaryDao): SalaryRepository =
        SalaryRepository(salaryDao)

    @Provides
    @Singleton
    fun provideSpendRepository(spendDao: SpendDao): SpendRepository =
        SpendRepository(spendDao)

    @Provides
    @Singleton
    fun provideCommuteRepository(commuteDao: CommuteDao): CommuteRepository =
        CommuteRepository(commuteDao)

    @Provides
    @Singleton
    fun provideBirthdayRepository(birthdayDao: BirthdayDao): BirthdayRepository =
        BirthdayRepository(birthdayDao)
}