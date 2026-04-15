package com.personal.kakeibox.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.kakeibox.data.preferences.AppLanguage
import com.personal.kakeibox.data.preferences.DarkThemePreference
import com.personal.kakeibox.data.preferences.NavBarStyle
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.data.preferences.UserPreferencesRepository
import com.personal.kakeibox.data.repository.CommuteRepository
import com.personal.kakeibox.data.repository.SalaryRepository
import com.personal.kakeibox.data.repository.SpendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository,
    private val spendRepository: SpendRepository,
    private val salaryRepository: SalaryRepository,
    private val commuteRepository: CommuteRepository
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

    fun setTabOrder(order: List<String>) {
        viewModelScope.launch {
            preferencesRepository.setTabOrder(order)
        }
    }

    fun setPrivacyModeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setPrivacyModeEnabled(enabled)
        }
    }

    fun exportToCsv(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val spends = spendRepository.getAllEntries().first()
                val salaries = salaryRepository.getAllEntries().first()
                val commutes = commuteRepository.getAllEntries().first()

                val csvBuilder = StringBuilder()
                
                // Spend Entries
                csvBuilder.append("SPENDING HISTORY\n")
                csvBuilder.append("Date,Description,Amount,Category,Note\n")
                spends.forEach {
                    csvBuilder.append("${it.month}/${it.year},${it.description},${it.amount},${it.category},${it.note}\n")
                }

                csvBuilder.append("\nSALARY HISTORY\n")
                csvBuilder.append("Date,Salary,Remittance,Savings,Note\n")
                salaries.forEach {
                    csvBuilder.append("${it.month}/${it.year},${it.salaryAmount},${it.remittanceAmount},${it.savingsAmount},${it.note}\n")
                }

                csvBuilder.append("\nCOMMUTE HISTORY\n")
                csvBuilder.append("Date,One Way Fare,Holidays,WFH Days,Total Cost\n")
                commutes.forEach {
                    val total = (it.oneWayFare * 2) * (20 - it.holidays - it.wfhDays) // simplified calc for export
                    csvBuilder.append("${it.createdAt},${it.oneWayFare},${it.holidays},${it.wfhDays},$total\n")
                }

                onResult(csvBuilder.toString())
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }
}
