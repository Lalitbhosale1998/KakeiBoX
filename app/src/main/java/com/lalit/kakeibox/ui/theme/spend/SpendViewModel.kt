package com.personal.kakeibox.ui.spend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.kakeibox.data.entity.SpendCategory
import com.personal.kakeibox.data.entity.SpendEntry
import com.personal.kakeibox.data.entity.SalaryEntry
import com.personal.kakeibox.data.repository.SpendRepository
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

data class SpendUiState(
    val currentMonth: Int = DateUtils.getCurrentMonth(),
    val currentYear: Int = DateUtils.getCurrentYear(),
    val selectedCategory: SpendCategory? = null, // null = show all
    val showAddEditSheet: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showHistorySheet: Boolean = false,
    val editingEntry: SpendEntry? = null,
    val deletingEntry: SpendEntry? = null,

    // Input fields
    val inputDescription: String = "",
    val inputAmount: String = "",
    val inputCategory: SpendCategory = SpendCategory.NEED,
    val inputNote: String = "",
    val inputMonth: Int = DateUtils.getCurrentMonth(),
    val inputYear: Int = DateUtils.getCurrentYear(),

    // Validation
    val descriptionError: String? = null,
    val amountError: String? = null,
    val snackbarMessage: String? = null
)

@HiltViewModel
class SpendViewModel @Inject constructor(
    private val spendRepository: SpendRepository,
    private val salaryRepository: SalaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpendUiState())
    val uiState: StateFlow<SpendUiState> = _uiState.asStateFlow()

    // All entries for viewed month
    private val _currentMonthEntries = MutableStateFlow<List<SpendEntry>>(emptyList())
    val currentMonthEntries: StateFlow<List<SpendEntry>> = _currentMonthEntries.asStateFlow()

    // All entries ever (for history)
    val allEntries = spendRepository.getAllEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Total NEED this month
    private val _totalNeedThisMonth = MutableStateFlow(0L)
    val totalNeedThisMonth: StateFlow<Long> = _totalNeedThisMonth.asStateFlow()

    // Total WANT this month
    private val _totalWantThisMonth = MutableStateFlow(0L)
    val totalWantThisMonth: StateFlow<Long> = _totalWantThisMonth.asStateFlow()

    // Total spend this month
    private val _totalSpendThisMonth = MutableStateFlow(0L)
    val totalSpendThisMonth: StateFlow<Long> = _totalSpendThisMonth.asStateFlow()

    // Current month salary — for 50/30/20 tip
    private val _currentSalary = MutableStateFlow<SalaryEntry?>(null)
    val currentSalary: StateFlow<SalaryEntry?> = _currentSalary.asStateFlow()

    init {
        observeMonthData()
    }

    private fun observeMonthData() {
        viewModelScope.launch {
            _uiState.collect { state ->
                // Update month entries
                spendRepository.getEntriesByMonthYear(state.currentMonth, state.currentYear).collect {
                    _currentMonthEntries.value = it
                }
            }
        }
        viewModelScope.launch {
            _uiState.collect { state ->
                spendRepository.getTotalByCategory(state.currentMonth, state.currentYear, SpendCategory.NEED).collect {
                    _totalNeedThisMonth.value = it ?: 0L
                }
            }
        }
        viewModelScope.launch {
            _uiState.collect { state ->
                spendRepository.getTotalByCategory(state.currentMonth, state.currentYear, SpendCategory.WANT).collect {
                    _totalWantThisMonth.value = it ?: 0L
                }
            }
        }
        viewModelScope.launch {
            _uiState.collect { state ->
                spendRepository.getTotalByMonthYear(state.currentMonth, state.currentYear).collect {
                    _totalSpendThisMonth.value = it ?: 0L
                }
            }
        }
        viewModelScope.launch {
            _uiState.collect { state ->
                salaryRepository.getEntryByMonthYear(state.currentMonth, state.currentYear).collect {
                    _currentSalary.value = it
                }
            }
        }
    }

    // ── Period Navigation ──────────────────────────────

    fun updateViewedMonth(month: Int) {
        _uiState.update { it.copy(currentMonth = month) }
    }

    fun updateViewedYear(year: Int) {
        _uiState.update { it.copy(currentYear = year) }
    }

    // ── Filter ────────────────────────────────────────

    fun setFilter(category: SpendCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    // ── Dialog Controls ───────────────────────────────

    fun openAddSheet(defaultCategory: SpendCategory = SpendCategory.NEED) {
        _uiState.update {
            it.copy(
                showAddEditSheet = true,
                editingEntry = null,
                inputDescription = "",
                inputAmount = "",
                inputCategory = defaultCategory,
                inputNote = "",
                inputMonth = DateUtils.getCurrentMonth(),
                inputYear = DateUtils.getCurrentYear(),
                descriptionError = null,
                amountError = null
            )
        }
    }

    fun openEditSheet(entry: SpendEntry) {
        _uiState.update {
            it.copy(
                showAddEditSheet = true,
                showHistorySheet = false, // close history if open
                editingEntry = entry,
                inputDescription = entry.description,
                inputAmount = entry.amount.toString(),
                inputCategory = entry.category,
                inputNote = entry.note,
                inputMonth = entry.month,
                inputYear = entry.year,
                descriptionError = null,
                amountError = null
            )
        }
    }

    fun closeSheet() {
        _uiState.update {
            it.copy(
                showAddEditSheet = false,
                editingEntry = null,
                descriptionError = null,
                amountError = null
            )
        }
    }

    fun openDeleteDialog(entry: SpendEntry) {
        _uiState.update {
            it.copy(showDeleteDialog = true, deletingEntry = entry, showHistorySheet = false)
        }
    }

    fun closeDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false, deletingEntry = null) }
    }

    fun toggleHistorySheet() {
        _uiState.update { it.copy(showHistorySheet = !it.showHistorySheet) }
    }

    // ── Input Updates ─────────────────────────────────

    fun updateDescription(value: String) {
        _uiState.update { it.copy(inputDescription = value, descriptionError = null) }
    }

    fun updateAmount(value: String) {
        _uiState.update { it.copy(inputAmount = value, amountError = null) }
    }

    fun updateCategory(value: SpendCategory) {
        _uiState.update { it.copy(inputCategory = value) }
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
        var hasError = false

        if (state.inputDescription.isBlank()) {
            _uiState.update { it.copy(descriptionError = "Description is required") }
            hasError = true
        }

        val amount = state.inputAmount.toLongOrNull()
        if (amount == null || amount <= 0L) {
            _uiState.update { it.copy(amountError = "Enter a valid amount") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            val entry = if (state.editingEntry != null) {
                state.editingEntry.copy(
                    description = state.inputDescription,
                    amount = amount!!,
                    category = state.inputCategory,
                    note = state.inputNote,
                    month = state.inputMonth,
                    year = state.inputYear,
                    updatedAt = System.currentTimeMillis()
                )
            } else {
                SpendEntry(
                    description = state.inputDescription,
                    amount = amount!!,
                    category = state.inputCategory,
                    note = state.inputNote,
                    month = state.inputMonth,
                    year = state.inputYear
                )
            }

            if (state.editingEntry != null) {
                spendRepository.update(entry)
            } else {
                spendRepository.insert(entry)
            }

            _uiState.update {
                it.copy(
                    showAddEditSheet = false,
                    editingEntry = null,
                    snackbarMessage = if (state.editingEntry != null)
                        "Entry updated" else "Entry added"
                )
            }
        }
    }

    fun deleteEntry() {
        val entry = _uiState.value.deletingEntry ?: return
        viewModelScope.launch {
            spendRepository.delete(entry)
            _uiState.update {
                it.copy(
                    showDeleteDialog = false,
                    deletingEntry = null,
                    snackbarMessage = "Entry deleted"
                )
            }
        }
    }

    fun deleteEntryDirectly(entry: SpendEntry) {
        viewModelScope.launch {
            spendRepository.delete(entry)
            _uiState.update { it.copy(snackbarMessage = "Entry deleted") }
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}