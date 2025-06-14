package com.ikhut.alarm.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.ikhut.alarm.data.AlarmDataStore
import com.ikhut.alarm.domain.model.Alarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlarmRepository(private val alarmDataStore: AlarmDataStore) {
    fun getAllAlarms(): LiveData<List<Alarm>> {
        return alarmDataStore.allAlarms.asLiveData()
    }

    suspend fun clearAllAlarms() {
        withContext(Dispatchers.IO) {
            alarmDataStore.clearAllAlarms()
        }
    }

    suspend fun insertAlarm(alarm: Alarm) {
        withContext(Dispatchers.IO) {
            alarmDataStore.insertAlarm(alarm)
        }
    }

    suspend fun updateAlarm(alarm: Alarm) {
        withContext(Dispatchers.IO) {
            alarmDataStore.updateAlarm(alarm)
        }
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        withContext(Dispatchers.IO) {
            alarmDataStore.deleteAlarm(alarm)
        }
    }
}