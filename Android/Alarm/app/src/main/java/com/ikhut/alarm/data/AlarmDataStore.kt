package com.ikhut.alarm.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ikhut.alarm.domain.model.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.alarmDataStore: DataStore<Preferences> by preferencesDataStore(name = "alarm_preferences")

class AlarmDataStore(private val context: Context) {

    private object PreferencesKeys {
        val ALARMS_LIST = stringPreferencesKey("alarms_list")
    }

    private val gson = Gson()

    val allAlarms: Flow<List<Alarm>> = context.alarmDataStore.data.map { preferences ->
        val alarmsJson = preferences[PreferencesKeys.ALARMS_LIST] ?: "[]"
        parseAlarmsFromJson(alarmsJson).sortedBy { it.minutes }
    }

    suspend fun insertAlarm(alarm: Alarm) {
        context.alarmDataStore.edit { preferences ->
            val currentAlarms = getCurrentAlarmsList(preferences).toMutableList()
            currentAlarms.add(alarm)
            preferences[PreferencesKeys.ALARMS_LIST] = gson.toJson(currentAlarms)
        }
    }

    suspend fun updateAlarm(alarm: Alarm) {
        context.alarmDataStore.edit { preferences ->
            val currentAlarms = getCurrentAlarmsList(preferences).toMutableList()
            val index = currentAlarms.indexOfFirst { it.id == alarm.id }

            if (index != -1) {
                currentAlarms[index] = alarm
                preferences[PreferencesKeys.ALARMS_LIST] = gson.toJson(currentAlarms)
            }
        }
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        context.alarmDataStore.edit { preferences ->
            val currentAlarms = getCurrentAlarmsList(preferences).toMutableList()
            currentAlarms.removeAll { it.id == alarm.id }
            preferences[PreferencesKeys.ALARMS_LIST] = gson.toJson(currentAlarms)
        }
    }

    suspend fun getAlarmById(alarmId: Long): Alarm? {
        return try {
            val preferences = context.alarmDataStore.data.first()
            val currentAlarms = getCurrentAlarmsList(preferences)
            currentAlarms.find { it.id == alarmId }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun clearAllAlarms() {
        context.alarmDataStore.edit { preferences ->
            preferences[PreferencesKeys.ALARMS_LIST] = "[]"
        }
    }

    private fun getCurrentAlarmsList(preferences: Preferences): List<Alarm> {
        val alarmsJson = preferences[PreferencesKeys.ALARMS_LIST] ?: "[]"
        return parseAlarmsFromJson(alarmsJson)
    }

    private fun parseAlarmsFromJson(json: String): List<Alarm> {
        return try {
            val type = object : TypeToken<List<Alarm>>() {}.type
            gson.fromJson<List<Alarm>>(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}