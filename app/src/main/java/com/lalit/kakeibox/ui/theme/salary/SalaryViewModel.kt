package com.personal.kakeibox.ui.salary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.repository.SalaryRepository
import com.personal.kakeibox.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import java.util.Calendar

enum class SalaryFilter {
    ALL, THIS_YEAR, HIGH_SAVINGS
}

enum class DonutDisplayMode {
    PERCENTAGE, ABSOLUTE, REMAINING_DAYS
}

// UI state for the salary screen
data class SalaryUiState(
    val currentMonth: Int = DateUtils.getCurrentMonth(),
    val currentYear: Int = DateUtils.getCurrentYear(),
    val isLoading: Boolean = false,
    val showAddEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val editingEntry: SalaryEntry? = null,  // null = adding new
    val deletingEntry: SalaryEntry? = null,
    val showHistorySheet: Boolean = false,

    // Dialog input fields
    val inputSalary: String = "",
    val inputRemittance: String = "",
    val inputSavings: String = "",
    val inputNote: String = "",
    val inputMonth: Int = DateUtils.getCurrentMonth(),
    val inputYear: Int = DateUtils.getCurrentYear(),

    // Validation
    val salaryError: String? = null,
    val snackbarMessage: String? = null,

    // Expressive Refinements
    val currentFilter: SalaryFilter = SalaryFilter.ALL,
    val donutMode: DonutDisplayMode = DonutDisplayMode.PERCENTAGE
)

@HiltViewModel
class SalaryViewModel @Inject constructor(
    private val repository: SalaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalaryUiState())
    val uiState: StateFlow<SalaryUiState> = _uiState.asStateFlow()

    // All entries for history
    val allEntries = repository.getAllEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Current month entry
    val currentEntry = repository.getEntryByMonthYear(
        DateUtils.getCurrentMonth(),
        DateUtils.getCurrentYear()
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Total savings across all months
    val totalSavings = repository.getTotalSavings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )

    // Total salary across all months
    val totalSalary = repository.getTotalSalary()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )

    // Total remittance across all months
    val totalRemittance = repository.getTotalRemittance()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0L
        )

    // ── Expressive Refinements ───────────────────────

    fun setFilter(filter: SalaryFilter) {
        _uiState.update { it.copy(currentFilter = filter) }
    }

    fun toggleDonutMode() {
        _uiState.update {
            val nextMode = when (it.donutMode) {
                DonutDisplayMode.PERCENTAGE -> DonutDisplayMode.ABSOLUTE
                DonutDisplayMode.ABSOLUTE -> DonutDisplayMode.REMAINING_DAYS
                DonutDisplayMode.REMAINING_DAYS -> DonutDisplayMode.PERCENTAGE
            }
            it.copy(donutMode = nextMode)
        }
    }

    // ── Dialog Controls ──────────────────────────────

    fun openAddDialog() {
        _uiState.update {
            it.copy(
                showAddEditDialog = true,
                editingEntry = null,
                inputSalary = "",
                inputRemittance = "",
                inputSavings = "",
                inputNote = "",
                inputMonth = DateUtils.getCurrentMonth(),
                inputYear = DateUtils.getCurrentYear(),
                salaryError = null
            )
        }
    }

    fun openEditDialog(entry: SalaryEntry) {
        _uiState.update {
            it.copy(
                showAddEditDialog = true,
                editingEntry = entry,
                inputSalary = entry.salaryAmount.toString(),
                inputRemittance = entry.remittanceAmount.toString(),
                inputSavings = entry.savingsAmount.toString(),
                inputNote = entry.note,
                inputMonth = entry.month,
                inputYear = entry.year,
                salaryError = null
            )
        }
    }

    fun closeDialog() {
        _uiState.update {
            it.copy(
                showAddEditDialog = false,
                editingEntry = null,
                salaryError = null
            )
        }
    }

    fun openDeleteDialog(entry: SalaryEntry) {
        _uiState.update { it.copy(showDeleteDialog = true, deletingEntry = entry) }
    }

    fun closeDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false, deletingEntry = null) }
    }

    fun toggleHistorySheet() {
        _uiState.update { it.copy(showHistorySheet = !it.showHistorySheet) }
    }

    // ── Input Updates ─────────────────────────────────

    fun updateSalary(value: String) {
        _uiState.update { it.copy(inputSalary = value, salaryError = null) }
    }

    fun updateRemittance(value: String) {
        _uiState.update { it.copy(inputRemittance = value) }
    }

    fun updateSavings(value: String) {
        _uiState.update { it.copy(inputSavings = value) }
    }

    fun updateNote(value: String) {
        _uiState.update { it.copy(inputNote = value) }
    }

    fun updateMonth(value: Int) {
        _uiState.update { it.copy(inputMonth = value) }
    }

    fun updateYear(value: Int) {
        _uiState.update { it.copy(inputYear = value) }
    }

    // ── Save / Delete ─────────────────────────────────

    fun saveEntry() {
        val state = _uiState.value

        // Validation
        val salary = state.inputSalary.toLongOrNull()
        if (salary == null || salary <= 0L) {
            _uiState.update { it.copy(salaryError = "Please enter a valid salary") }
            return
        }

        val remittance = state.inputRemittance.toLongOrNull() ?: 0L
        val savings = state.inputSavings.toLongOrNull() ?: 0L
        val remaining = salary - remittance - savings

        viewModelScope.launch {
            val entry = if (state.editingEntry != null) {
                // Editing existing
                state.editingEntry.copy(
                    salaryAmount = salary,
                    remittanceAmount = remittance,
                    savingsAmount = savings,
                    remainingAmount = remaining,
                    note = state.inputNote,
                    month = state.inputMonth,
                    year = state.inputYear,
                    updatedAt = System.currentTimeMillis()
                )
            } else {
                // Adding new
                SalaryEntry(
                    salaryAmount = salary,
                    remittanceAmount = remittance,
                    savingsAmount = savings,
                    remainingAmount = remaining,
                    note = state.inputNote,
                    month = state.inputMonth,
                    year = state.inputYear
                )
            }

            if (state.editingEntry != null) {
                repository.update(entry)
            } else {
                repository.insert(entry)
            }

            _uiState.update {
                it.copy(
                    showAddEditDialog = false,
                    editingEntry = null,
                    snackbarMessage = if (state.editingEntry != null)
                        "Entry updated" else "Entry saved"
                )
            }
        }
    }

    fun deleteEntry() {
        val entry = _uiState.value.deletingEntry ?: return
        viewModelScope.launch {
            repository.delete(entry)
            _uiState.update {
                it.copy(
                    showDeleteDialog = false,
                    deletingEntry = null,
                    snackbarMessage = "Entry deleted"
                )
            }
        }
    }

    fun deleteEntryDirectly(entry: SalaryEntry) {
        viewModelScope.launch {
            repository.delete(entry)
            _uiState.update { it.copy(snackbarMessage = "Entry deleted") }
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    init {
        addDummyData()
    }

    fun addDummyData() {
        val dummyData = listOf(
            "2024-12" to Pair(194068L, 16600L),
            "2025-01" to Pair(224094L, 16600L),
            "2025-02" to Pair(198981L, 16600L),
            "2025-03" to Pair(213838L, 16600L),
            "2025-04" to Pair(233014L, 16600L),
            "2025-05" to Pair(229988L, 16600L),
            "2025-06" to Pair(256836L, 16600L),
            "2025-07" to Pair(241884L, 16600L),
            "2025-08" to Pair(256816L, 16600L),
            "2025-09" to Pair(252330L, 16600L),
            "2025-10" to Pair(227927L, 16600L),
            "2025-11" to Pair(174626L, 16600L),
            "2025-12" to Pair(182330L, 16600L),
            "2026-01" to Pair(228301L, 16600L),
            "2026-02" to Pair(223663L, 16600L),
            "2026-03" to Pair(682368L, 300000L),
            "2026-04" to Pair(497204L, 300000L),
            "2026-05" to Pair(363021L, 250000L),
            "2026-06" to Pair(495107L, 350000L),
            "2026-07" to Pair(429664L, 284200L)
        )

        viewModelScope.launch {
            var updatedCount = 0
            dummyData.forEach { (date, pair) ->
                val parts = date.split("-")
                val year = parts[0].toInt()
                val month = parts[1].toInt()
                val salaryAmount = pair.first
                val remittanceAmount = pair.second

                val existing = repository.getEntryByMonthYearOnce(month, year)
                if (existing == null) {
                    val entry = SalaryEntry(
                        month = month,
                        year = year,
                        salaryAmount = salaryAmount,
                        remittanceAmount = remittanceAmount,
                        savingsAmount = 0L,
                        remainingAmount = salaryAmount - remittanceAmount,
                        note = "Salary Entry"
                    )
                    repository.insert(entry)
                    updatedCount++
                } else {
                    val updated = existing.copy(
                        salaryAmount = salaryAmount,
                        remittanceAmount = remittanceAmount,
                        savingsAmount = 0L,
                        remainingAmount = salaryAmount - remittanceAmount
                    )
                    repository.update(updated)
                    updatedCount++
                }
            }
            if (updatedCount > 0) {
                _uiState.update { it.copy(snackbarMessage = "Updated $updatedCount salary entries") }
            }
        }
    }
}