package com.personal.kakeibox.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.kakeibox.data.entity.ExerciseEntry
import com.personal.kakeibox.data.preferences.UserPreferencesRepository
import com.personal.kakeibox.data.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

data class ExerciseUiState(
    val selectedDay: String = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US),
    val restDays: List<String> = emptyList(),
    val exercises: List<ExerciseEntry> = emptyList(),
    val isRestDay: Boolean = false,
    val showAddEditSheet: Boolean = false,
    val selectedExercise: ExerciseEntry? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _selectedDay = MutableStateFlow(
        LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)
    )
    val selectedDay = _selectedDay.asStateFlow()

    private val _showAddEditSheet = MutableStateFlow(false)
    val showAddEditSheet = _showAddEditSheet.asStateFlow()

    private val _selectedExercise = MutableStateFlow<ExerciseEntry?>(null)
    val selectedExercise = _selectedExercise.asStateFlow()

    val themeSettings = preferencesRepository.themeSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val uiState: StateFlow<ExerciseUiState> = combine(
        _selectedDay,
        preferencesRepository.themeSettings,
        _showAddEditSheet,
        _selectedExercise
    ) { day, settings, showSheet, selectedExercise ->
        val restDays = settings.restDays
        val isRest = restDays.contains(day)
        Tuple5(day, restDays, showSheet, selectedExercise, isRest)
    }.flatMapLatest { tuple ->
        val (day, restDays, showSheet, selectedExercise, isRest) = tuple
        exerciseRepository.getExercisesForDay(day).combine(
            kotlinx.coroutines.flow.flowOf(Pair(restDays, isRest))
        ) { exerciseList, restPair ->
            ExerciseUiState(
                selectedDay = day,
                restDays = restPair.first,
                exercises = exerciseList,
                isRestDay = restPair.second,
                showAddEditSheet = showSheet,
                selectedExercise = selectedExercise
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExerciseUiState()
    )

    fun selectDay(day: String) {
        _selectedDay.value = day
    }

    fun openAddSheet() {
        _selectedExercise.value = null
        _showAddEditSheet.value = true
    }

    fun openEditSheet(exercise: ExerciseEntry) {
        _selectedExercise.value = exercise
        _showAddEditSheet.value = true
    }

    fun closeAddEditSheet() {
        _showAddEditSheet.value = false
        _selectedExercise.value = null
    }

    fun saveExercise(name: String, sets: Int, reps: Int, description: String, day: String) {
        viewModelScope.launch {
            val current = _selectedExercise.value
            if (current != null) {
                exerciseRepository.update(
                    current.copy(
                        name = name,
                        sets = sets,
                        reps = reps,
                        description = description,
                        dayOfWeek = day
                    )
                )
            } else {
                exerciseRepository.insert(
                    ExerciseEntry(
                        name = name,
                        sets = sets,
                        reps = reps,
                        description = description,
                        dayOfWeek = day
                    )
                )
            }
            closeAddEditSheet()
        }
    }

    fun deleteExercise(exercise: ExerciseEntry) {
        viewModelScope.launch {
            exerciseRepository.delete(exercise)
        }
    }

    fun toggleRestDay(day: String) {
        viewModelScope.launch {
            val currentSettings = themeSettings.value ?: return@launch
            val currentRestDays = currentSettings.restDays.toMutableList()
            if (currentRestDays.contains(day)) {
                currentRestDays.remove(day)
            } else {
                currentRestDays.add(day)
            }
            preferencesRepository.setRestDays(currentRestDays)
        }
    }
}

private data class Tuple5<T1, T2, T3, T4, T5>(
    val v1: T1,
    val v2: T2,
    val v3: T3,
    val v4: T4,
    val v5: T5
)
