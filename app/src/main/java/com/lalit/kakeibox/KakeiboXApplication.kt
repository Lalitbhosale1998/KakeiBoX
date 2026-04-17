package com.personal.kakeibox

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.personal.kakeibox.worker.BirthdayWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class KakeiboXApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleBirthdayWorker()
    }

    private fun scheduleBirthdayWorker() {
        val workRequest = PeriodicWorkRequestBuilder<BirthdayWorker>(1, TimeUnit.DAYS)
            .addTag("birthday_worker")
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "birthday_notification_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}