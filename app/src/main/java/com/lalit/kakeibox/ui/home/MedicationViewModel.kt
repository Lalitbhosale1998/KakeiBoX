package com.personal.kakeibox.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.kakeibox.data.entity.MedicationEntry
import com.personal.kakeibox.data.entity.repository.MedicationRepository
import com.personal.kakeibox.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val repository: MedicationRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val todayDateStr = LocalDate.now().toString()

    val todayLog: StateFlow<MedicationEntry?> = repository.getLogByDate(todayDateStr)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val fullyCompletedDaysCount: StateFlow<Int> = repository.getFullyCompletedDaysCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun toggleDose(doseType: String) {
        viewModelScope.launch {
            repository.toggleDose(todayDateStr, doseType)
        }
    }

    fun updateTimes(breakfast: String, lunch: String, dinner: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateMedicationTimes(breakfast, lunch, dinner)
        }
    }
}
