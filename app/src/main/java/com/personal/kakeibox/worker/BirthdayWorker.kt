package com.personal.kakeibox.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.personal.kakeibox.R
import com.personal.kakeibox.data.repository.BirthdayRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@HiltWorker
class BirthdayWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: BirthdayRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val today = LocalDate.now()
        val birthdays = repository.getAllBirthdays().first()
        
        val celebrants = birthdays.filter { 
            it.isEnabled && it.date.month == today.month && it.date.dayOfMonth == today.dayOfMonth 
        }

        if (celebrants.isNotEmpty()) {
            celebrants.forEach { birthday ->
                showNotification(birthday.name)
            }
        }

        return Result.success()
    }

    private fun showNotification(name: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "birthday_reminders"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Birthday Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for birthday reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use a proper icon if available
            .setContentTitle("Birthday Reminder! 🎂")
            .setContentText("It's $name's birthday today! Don't forget to wish them.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(name.hashCode(), notification)
    }
}