package com.ikhut.weatherapp

import java.util.Calendar
import java.util.TimeZone

object TimeUtils {
    fun getLocalHour(timestamp: Long, timezoneOffset: Int): Int {
        val millis = if (timestamp.toString().length > 10) timestamp else timestamp * 1000
        val timeZone = TimeZone.getTimeZone("GMT").apply {
            rawOffset = timezoneOffset * 1000
        }
        val calendar = Calendar.getInstance(timeZone).apply {
            timeInMillis = millis
        }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
}
