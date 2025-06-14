package com.ikhut.alarm.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ikhut.alarm.domain.model.Alarm
import com.ikhut.alarm.presentation.service.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmReceiver : BroadcastReceiver() {
    private val notificationService by lazy { NotificationService() }

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val alarmId = intent.getLongExtra(EXTRA_ALARM_ID, -1L)
        if (alarmId == -1L) {
            return
        }

        when (intent.action) {
            ACTION_ALARM_TRIGGER -> handleAlarmTrigger(context, intent, alarmId)
            ACTION_ALARM_SNOOZE -> handleAlarmSnooze(context, intent, alarmId)
            ACTION_ALARM_CANCEL -> handleAlarmCancel(context, alarmId)
            else -> Log.w(TAG, "Unknown action: ${intent.action}")
        }
    }

    private fun handleAlarmTrigger(context: Context, intent: Intent, alarmId: Long) {
        val alarmMinutes = intent.getIntExtra(EXTRA_ALARM_MINUTES, -1)
        if (alarmMinutes == -1) {
            return
        }

        val alarm = Alarm(id = alarmId, minutes = alarmMinutes, isOn = true)
        val alarmTime = alarm.getFormattedTime()

        notificationService.showAlarmNotification(context, alarmId.toInt(), alarmTime, alarmMinutes)

        disableAlarmAfterTrigger(context.applicationContext, alarmId)
    }

    private fun disableAlarmAfterTrigger(context: Context, alarmId: Long) {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            try {
                val alarmDataStore = AlarmDataStore(context)
                val alarm = alarmDataStore.getAlarmById(alarmId)

                if (alarm != null) {
                    val disabledAlarm = alarm.copy(isOn = false)
                    alarmDataStore.updateAlarm(disabledAlarm)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error disabling alarm after trigger: ${e.message}")
            }
        }
    }

    private fun handleAlarmSnooze(context: Context, intent: Intent, alarmId: Long) {
        val alarmMinutes = intent.getIntExtra(EXTRA_ALARM_MINUTES, -1)
        if (alarmMinutes == -1) {
            return
        }

        try {
            createSnoozeAlarm(context, alarmId)
            notificationService.dismissNotification(context, alarmId.toInt())
        } catch (e: Exception) {
            Log.e(TAG, "Error snoozing alarm: ${e.message}")
        }
    }

    private fun handleAlarmCancel(context: Context, alarmId: Long) {
        try {
            notificationService.dismissNotification(context, alarmId.toInt())
        } catch (e: Exception) {
            Log.e(TAG, "Error canceling alarm: ${e.message}")
        }
    }

    private fun createSnoozeAlarm(context: Context, alarmId: Long) {
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)
        val currentTotalMinutes = Alarm.toMinutes(currentHour, currentMinute)
        val snoozeMinutes = currentTotalMinutes + 1

        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            try {
                val alarmDataStore = AlarmDataStore(context.applicationContext)
                val allAlarms = alarmDataStore.allAlarms.first()

                val hasConflict = allAlarms.any { alarm ->
                    alarm.minutes == snoozeMinutes && alarm.isOn && alarm.id != alarmId
                }

                if (hasConflict) {
                    return@launch
                }

                val snoozeAlarm = Alarm(
                    id = alarmId, minutes = snoozeMinutes, isOn = true
                )

                val alarmScheduler = AlarmScheduler(context)
                alarmDataStore.insertAlarm(snoozeAlarm)
                alarmScheduler.scheduleAlarm(snoozeAlarm)
            } catch (e: Exception) {
                Log.e(TAG, "Error checking for alarm conflicts: ${e.message}")
            }
        }
    }

    companion object {
        const val ACTION_ALARM_TRIGGER = "com.ikhut.alarm.ACTION_ALARM_TRIGGER"
        const val ACTION_ALARM_SNOOZE = "com.ikhut.alarm.ACTION_ALARM_SNOOZE"
        const val ACTION_ALARM_CANCEL = "com.ikhut.alarm.ACTION_ALARM_CANCEL"

        private const val TAG = "AlarmReceiver"

        const val EXTRA_ALARM_ID = "extra_alarm_id"
        const val EXTRA_ALARM_MINUTES = "extra_alarm_minutes"
    }
}