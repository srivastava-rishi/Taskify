package com.rsstudio.taskify.jobs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.rsstudio.taskify.common.alias.AppDrawable
import com.rsstudio.taskify.common.alias.AppId
import com.rsstudio.taskify.common.alias.AppLayout
import java.util.concurrent.TimeUnit


class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val title = inputData.getString("title") ?: "Reminder"
        val description = inputData.getString("description") ?: "It's time!"
        val time = inputData.getString("time") ?: "2:00PM"
        val fromApi = inputData.getBoolean("fromApi", false)
        showCustomNotification(applicationContext, title, description, time, fromApi)
        return Result.success()
    }

    private fun showCustomNotification(
        context: Context,
        title: String,
        description: String,
        time: String,
        fromApi: Boolean
    ) {
        val channelId = "REMINDER_CHANNEL"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val remoteViews = RemoteViews(context.packageName, AppLayout.custom_notification).apply {
            setTextViewText(AppId.notificationTitle, title)
            setTextViewText(AppId.notificationDescription, description)
            setTextViewText(AppId.notificationTimestamp, time)
            if (fromApi) {
                setViewVisibility(AppId.tvNotificationChip, View.VISIBLE)
            } else {
                setViewVisibility(AppId.tvNotificationChip, View.GONE)
            }
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(AppDrawable.bell_ring)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {

        fun scheduleWork(
            scheduleWork: Boolean = true,
            context: Context,
            interval: Long,
            intervalUnit: TimeUnit = TimeUnit.DAYS,
            tag: String,
            title: String,
            description: String,
            time: String,
            fromApi: Boolean
        ) {
            if (!scheduleWork) return
            val workManager = WorkManager.getInstance(context)
            val inputData = workDataOf(
                "title" to title,
                "description" to description,
                "time" to time,
                "fromApi" to fromApi
            )
            val workRequest = PeriodicWorkRequest.Builder(
                ReminderWorker::class.java,
                interval,
                intervalUnit
            ).setInputData(inputData)
                .addTag(tag)
                .setInitialDelay(15,TimeUnit.MINUTES)
                .build()

            workManager.enqueueUniquePeriodicWork(
                tag,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
        }

        fun cancelWork(context: Context, tag: String) {
            WorkManager.getInstance(context).cancelAllWorkByTag(tag)
        }
    }
}
