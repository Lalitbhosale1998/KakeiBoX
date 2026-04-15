package com.personal.kakeibox.ui.commute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.kakeibox.data.entity.CommuteEntry
import com.personal.kakeibox.data.repository.CommuteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class CommuteUiState(
    val latestEntry: CommuteEntry? = null,
    val history: List<CommuteEntry> = emptyList(),
    val totalCostAllTime: Long = 0L,
    val showAddSheet: Boolean = false,
    val showHistorySheet: Boolean = false,
    val deletingEntry: CommuteEntry? = null,
    val showDeleteDialog: Boolean = false,
    val snackbarMessage: String? = null,
    
    // Inputs
    val inputOneWayFare: String = "",
    val inputHolidays: String = "0",
    val inputWfhDays: String = "0",
    
    // Validation
    val fareError: String? = null
)

@HiltViewModel
class CommuteViewModel @Inject constructor(
    private val repository: CommuteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommuteUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllEntries().collect { entries ->
                _uiState.update { it.copy(
                    history = entries,
                    latestEntry = entries.firstOrNull()
                ) }
            }
        }
        viewModelScope.launch {
            repository.getTotalCostAllTime().collect { total ->
                _uiState.update { it.copy(totalCostAllTime = total ?: 0L) }
            }
        }
    }

    fun openAddSheet() {
        _uiState.update { it.copy(showAddSheet = true) }
    }

    fun closeAddSheet() {
        _uiState.update { it.copy(showAddSheet = false, inputOneWayFare = "", fareError = null) }
    }

    fun updateFare(value: String) {
        _uiState.update { it.copy(inputOneWayFare = value, fareError = null) }
    }

    fun updateHolidays(value: String) {
        _uiState.update { it.copy(inputHolidays = value) }
    }

    fun updateWfhDays(value: String) {
        _uiState.update { it.copy(inputWfhDays = value) }
    }

    fun saveEntry() {
        val fare = _uiState.value.inputOneWayFare.toLongOrNull()
        if (fare == null || fare <= 0) {
            _uiState.update { it.copy(fareError = "Invalid fare") }
            return
        }

        val holidays = _uiState.value.inputHolidays.toIntOrNull() ?: 0
        val wfh = _uiState.value.inputWfhDays.toIntOrNull() ?: 0

        viewModelScope.launch {
            // Basic logic for now - can be refined
            val calendar = Calendar.getInstance()
            val start = calendar.timeInMillis
            calendar.add(Calendar.MONTH, 1)
            val end = calendar.timeInMillis

            val workingDays = 22 // Average
            val commuteDays = workingDays - holidays - wfh
            val totalCost = (fare * 2) * commuteDays

            val entry = CommuteEntry(
                oneWayFare = fare,
                holidays = holidays,
                wfhDays = wfh,
                periodStartDate = start,
                periodEndDate = end,
                totalWorkingDays = workingDays,
                totalCommuteDays = maxOf(0, commuteDays),
                totalCost = maxOf(0, totalCost.toLong())
            )
            repository.insert(entry)
            _uiState.update { it.copy(snackbarMessage = "Entry added") }
            closeAddSheet()
        }
    }

    fun deleteEntry(entry: CommuteEntry) {
        viewModelScope.launch {
            repository.delete(entry)
            _uiState.update { it.copy(
                showDeleteDialog = false, 
                deletingEntry = null,
                snackbarMessage = "Entry deleted"
            ) }
        }
    }

    fun openDeleteDialog(entry: CommuteEntry) {
        _uiState.update { it.copy(showDeleteDialog = true, deletingEntry = entry) }
    }

    fun closeDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false, deletingEntry = null) }
    }

    fun toggleHistory() {
        _uiState.update { it.copy(showHistorySheet = !it.showHistorySheet) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
