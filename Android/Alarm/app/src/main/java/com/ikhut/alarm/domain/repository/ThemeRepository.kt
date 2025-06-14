package com.ikhut.alarm.domain.repository

import com.ikhut.alarm.data.ThemePreference
import kotlinx.coroutines.flow.Flow

class ThemeRepository(private val themePreferenceDataStore: ThemePreference) {
    fun getDarkThemeStatus(): Flow<Boolean> {
        return themePreferenceDataStore.isDarkTheme
    }

    suspend fun setDarkThemeStatus(enableDarkTheme: Boolean) {
        themePreferenceDataStore.setDarkTheme(enableDarkTheme)
    }
}
