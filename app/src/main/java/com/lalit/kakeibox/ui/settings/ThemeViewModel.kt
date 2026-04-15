package com.personal.kakeibox.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.kakeibox.data.preferences.AppLanguage
import com.personal.kakeibox.data.preferences.DarkThemePreference
import com.personal.kakeibox.data.preferences.NavBarStyle
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val themeSettings: StateFlow<ThemeSettings> = preferencesRepository.themeSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeSettings()
        )

    fun setDarkThemePreference(value: DarkThemePreference) {
        viewModelScope.launch {
            preferencesRepository.setDarkThemePreference(value)
        }
    }

    fun setUseDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setUseDynamicColor(enabled)
        }
    }

    fun setNavBarStyle(style: NavBarStyle) {
        viewModelScope.launch {
            preferencesRepository.setNavBarStyle(style)
        }
    }

    fun setRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setRemindersEnabled(enabled)
        }
    }

    fun setCurrencySymbol(symbol: String) {
        viewModelScope.launch {
            preferencesRepository.setCurrencySymbol(symbol)
        }
    }

    fun setDateFormat(format: String) {
        viewModelScope.launch {
            preferencesRepository.setDateFormat(format)
        }
    }

    fun setAppLanguage(language: AppLanguage) {
        viewModelScope.launch {
            preferencesRepository.setAppLanguage(language)
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setBiometricEnabled(enabled)
        }
    }
}
