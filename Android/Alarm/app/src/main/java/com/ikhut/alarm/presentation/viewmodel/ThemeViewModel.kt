package com.ikhut.alarm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ikhut.alarm.domain.repository.ThemeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeViewModel(private val themeRepository: ThemeRepository) : ViewModel() {
    val isDarkTheme: LiveData<Boolean> = themeRepository.getDarkThemeStatus().asLiveData()

    fun toggleTheme() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTheme = isDarkTheme.value ?: false
            themeRepository.setDarkThemeStatus(!currentTheme)
        }
    }

    companion object {
        fun create(repository: ThemeRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ThemeViewModel(repository) as T
                }
            }
        }
    }
}