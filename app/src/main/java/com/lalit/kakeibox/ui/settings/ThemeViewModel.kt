package com.personal.kakeibox.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.kakeibox.data.preferences.AppLanguage
import com.personal.kakeibox.data.preferences.DarkThemePreference
import com.personal.kakeibox.data.preferences.NavBarStyle
import com.personal.kakeibox.data.preferences.TopAppBarBackground
import com.personal.kakeibox.data.preferences.ThemeSettings
import com.personal.kakeibox.data.preferences.UserPreferencesRepository
import com.personal.kakeibox.data.repository.BirthdayRepository
import com.personal.kakeibox.data.repository.CommuteRepository
import com.personal.kakeibox.data.repository.SalaryRepository
import com.personal.kakeibox.data.repository.SpendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.Executor
import javax.inject.Inject
import com.personal.kakeibox.data.entity.BirthdayEntry

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository,
    private val spendRepository: SpendRepository,
    private val salaryRepository: SalaryRepository,
    private val commuteRepository: CommuteRepository,
    private val birthdayRepository: BirthdayRepository
) : ViewModel() {

    private val _isAuthenticated = mutableStateOf(false)
    val isAuthenticated: State<Boolean> = _isAuthenticated

    val themeSettings: StateFlow<ThemeSettings> = preferencesRepository.themeSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeSettings()
        )

    val birthdays: StateFlow<List<BirthdayEntry>> = birthdayRepository.getAllBirthdays()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _showBirthdaySheet = MutableStateFlow(false)
    val showBirthdaySheet = _showBirthdaySheet.asStateFlow()

    fun toggleBirthdaySheet(show: Boolean) {
        _showBirthdaySheet.value = show
    }

    fun addBirthday(name: String, date: LocalDate) {
        viewModelScope.launch {
            birthdayRepository.insertBirthday(BirthdayEntry(name = name, date = date))
        }
    }

    fun deleteBirthday(birthday: BirthdayEntry) {
        viewModelScope.launch {
            birthdayRepository.deleteBirthday(birthday)
        }
    }

    fun updateBirthday(birthday: BirthdayEntry) {
        viewModelScope.launch {
            birthdayRepository.updateBirthday(birthday)
        }
    }

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
            
            val localeTag = when (language) {
                AppLanguage.ENGLISH -> "en"
                AppLanguage.JAPANESE -> "ja"
            }
            
            try {
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(localeTag)
                )
            } catch (e: Exception) {
                // Fallback for non-AppCompat environments if needed, 
                // but we are using FragmentActivity which should work.
            }
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setBiometricEnabled(enabled)
        }
    }

    fun authenticate(activity: FragmentActivity, executor: Executor) {
        val biometricManager = BiometricManager.from(activity)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        
        if (biometricManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS) {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock KakeiboX")
                .setSubtitle("Authenticate to access your financial data")
                .setAllowedAuthenticators(authenticators)
                .build()

            val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    _isAuthenticated.value = true
                }
            })

            biometricPrompt.authenticate(promptInfo)
        } else {
            // Biometric not available, let them in or handle accordingly
            _isAuthenticated.value = true
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

    fun setTopAppBarBackground(background: TopAppBarBackground) {
        viewModelScope.launch {
            preferencesRepository.setTopAppBarBackground(background)
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
