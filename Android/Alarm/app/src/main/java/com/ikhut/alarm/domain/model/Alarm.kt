package com.ikhut.alarm.domain.model

import android.annotation.SuppressLint

data class Alarm(
    val id: Long = System.currentTimeMillis(), val minutes: Int, val isOn: Boolean = true
) {

    fun getHour(): Int = minutes / 60

    fun getMinute(): Int = minutes % 60

    @SuppressLint("DefaultLocale")
    fun getFormattedTime(): String {
        val hour = getHour()
        val minute = getMinute()
        return String.format("%02d:%02d", hour, minute)
    }

    companion object {
        fun fromHourMinute(hour: Int, minute: Int, isOn: Boolean = true): Alarm {
            val totalMinutes = toMinutes(hour, minute)
            return Alarm(
                minutes = totalMinutes, isOn = isOn
            )
        }

        fun toMinutes(hour: Int, minute: Int): Int = (hour * 60) + minute
    }
}