package com.ikhut.alarm.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ikhut.alarm.data.AlarmReceiver

class NotificationService {

    fun showAlarmNotification(context: Context, alarmId: Int, time: String, alarmMinutes: Int) {
        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(ALARM_CONTENT_TITLE).setContentText("Alarm set for $time")
            .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true)
            .setContentIntent(createOpenAppPendingIntent(context, alarmId)).addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                CANCEL_ALARM_ACTION_TEXT,
                createCancelPendingIntent(context, alarmId)
            ).addAction(
                android.R.drawable.ic_menu_recent_history,
                SNOOZE_ALARM_ACTION_TEXT,
                createSnoozePendingIntent(context, alarmId, alarmMinutes)
            ).build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(alarmId, notification)
    }

    fun dismissNotification(context: Context, alarmId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(alarmId)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ALARM_CHANNEL_ID, ALARM_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for alarm notifications"
                enableVibration(true)
                setShowBadge(true)
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createOpenAppPendingIntent(context: Context, alarmId: Int): PendingIntent {
        val packageManager = context.packageManager
        val openAppIntent =
            packageManager.getLaunchIntentForPackage(context.packageName) ?: Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        return PendingIntent.getActivity(
            context,
            REQUEST_CODE_OPEN_APP + alarmId,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createCancelPendingIntent(context: Context, alarmId: Int): PendingIntent {
        val cancelIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_ALARM_CANCEL
            putExtra(EXTRA_ALARM_ID, alarmId.toLong())
        }

        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_CANCEL + alarmId,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createSnoozePendingIntent(
        context: Context, alarmId: Int, alarmMinutes: Int
    ): PendingIntent {
        val snoozeIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_ALARM_SNOOZE
            putExtra(EXTRA_ALARM_ID, alarmId.toLong())
            putExtra(EXTRA_ALARM_MINUTES, alarmMinutes)
        }

        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_SNOOZE + alarmId,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        private const val ALARM_CHANNEL_ID = "alarm_channel"
        private const val ALARM_CHANNEL_NAME = "Alarm Notifications"

        private const val ALARM_CONTENT_TITLE = "Alarm message"
        private const val CANCEL_ALARM_ACTION_TEXT = "CANCEL"
        private const val SNOOZE_ALARM_ACTION_TEXT = "SNOOZE"

        private const val REQUEST_CODE_OPEN_APP = 100
        private const val REQUEST_CODE_CANCEL = 101
        private const val REQUEST_CODE_SNOOZE = 102

        private const val EXTRA_ALARM_ID = "extra_alarm_id"
        private const val EXTRA_ALARM_MINUTES = "extra_alarm_minutes"
    }
}