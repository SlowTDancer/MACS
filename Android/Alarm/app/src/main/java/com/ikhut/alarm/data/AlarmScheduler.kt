package com.ikhut.alarm.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.ikhut.alarm.domain.model.Alarm
import java.util.Calendar

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(alarm: Alarm) {
        if (!alarm.isOn) {
            return
        }

        try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    if (alarmManager.canScheduleExactAlarms()) {
                        scheduleExactAlarm(alarm)
                    } else {
                        scheduleInexactAlarm(alarm)
                    }
                }

                else -> scheduleExactAlarm(alarm)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to schedule alarm ${alarm.id}: ${e.message}")
        }
    }

    private fun cancelAlarm(alarmId: Long, minutes: Int) {
        try {
            val intent = createAlarmIntent(alarmId, minutes)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId.toInt(),
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cancel alarm $alarmId: ${e.message}")
        }
    }

    fun cancelAlarm(alarm: Alarm) {
        cancelAlarm(alarm.id, alarm.minutes)
    }

    fun scheduleSnoozeAlarm(originalAlarm: Alarm, snoozeMinutes: Int = DEFAULT_SNOOZE_MINUTES) {
        val snoozeAlarm = originalAlarm.copy(
            minutes = originalAlarm.minutes + snoozeMinutes, isOn = true
        )

        scheduleAlarm(snoozeAlarm)
        Log.d(
            TAG,
            "Scheduled snooze alarm for ${snoozeAlarm.getFormattedTime()} (${snoozeMinutes} minutes)"
        )
    }

    fun rescheduleAlarm(oldAlarm: Alarm, newAlarm: Alarm) {
        cancelAlarm(oldAlarm)
        scheduleAlarm(newAlarm)
        Log.d(
            TAG,
            "Rescheduled alarm ${oldAlarm.id} from ${oldAlarm.getFormattedTime()} to ${newAlarm.getFormattedTime()}"
        )
    }

    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun getNextAlarmTimeMillis(alarm: Alarm): Long {
        return createCalendarFromAlarm(alarm).timeInMillis
    }

    fun isAlarmScheduled(alarmId: Long, minutes: Int): Boolean {
        return try {
            val intent = createAlarmIntent(alarmId, minutes)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId.toInt(),
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            pendingIntent != null
        } catch (e: Exception) {
            false
        }
    }

    private fun scheduleExactAlarm(alarm: Alarm) {
        val (_, pendingIntent, calendar) = createAlarmComponents(alarm)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
        )
    }

    private fun scheduleInexactAlarm(alarm: Alarm) {
        val (_, pendingIntent, calendar) = createAlarmComponents(alarm)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
        )
    }

    private fun createAlarmComponents(alarm: Alarm): Triple<Intent, PendingIntent, Calendar> {
        val intent = createAlarmIntent(alarm.id, alarm.minutes)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = createCalendarFromAlarm(alarm)

        return Triple(intent, pendingIntent, calendar)
    }

    private fun createAlarmIntent(alarmId: Long, minutes: Int): Intent {
        return Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_ALARM_TRIGGER
            putExtra(EXTRA_ALARM_ID, alarmId)
            putExtra(EXTRA_ALARM_MINUTES, minutes)
        }
    }

    private fun createCalendarFromAlarm(alarm: Alarm): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.getHour())
            set(Calendar.MINUTE, alarm.getMinute())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
    }

    companion object {
        private const val TAG = "AlarmScheduler"
        private const val DEFAULT_SNOOZE_MINUTES = 1

        const val EXTRA_ALARM_ID = "extra_alarm_id"
        const val EXTRA_ALARM_MINUTES = "extra_alarm_minutes"
    }
}