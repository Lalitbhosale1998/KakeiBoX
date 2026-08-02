package com.personal.kakeibox.ui.vocab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.kakeibox.data.entity.JLPTN1SeedData
import com.personal.kakeibox.data.entity.VocabEntry
import com.personal.kakeibox.data.entity.repository.VocabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabViewModel @Inject constructor(
    private val repository: VocabRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedSubCategory = MutableStateFlow("All")
    val selectedSubCategory: StateFlow<String> = _selectedSubCategory.asStateFlow()

    private val _selectedStudyTag = MutableStateFlow("All")
    val selectedStudyTag: StateFlow<String> = _selectedStudyTag.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val allEntries: StateFlow<List<VocabEntry>> = repository.allEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Auto-seed Week 1 Day 1 64 JLPT N1 words if database is brand new
        viewModelScope.launch {
            allEntries.first { entries ->
                if (entries.isEmpty()) {
                    JLPTN1SeedData.week1Day1Entries.forEach { seedEntry ->
                        repository.insertEntry(seedEntry)
                    }
                }
                true
            }
        }
    }

    val availableCategories: StateFlow<List<String>> = allEntries
        .map { entries ->
            val set = mutableSetOf("All")
            entries.forEach { if (it.category.isNotBlank()) set.add(it.category) }
            set.toList()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("All"))

    val availableSubCategories: StateFlow<List<String>> = allEntries
        .map { entries ->
            val set = mutableSetOf("All")
            entries.forEach { if (it.subCategory.isNotBlank()) set.add(it.subCategory) }
            set.toList()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("All"))

    val availableStudyTags: StateFlow<List<String>> = allEntries
        .map { entries ->
            val set = mutableSetOf("All")
            entries.forEach { if (it.studyTag.isNotBlank()) set.add(it.studyTag) }
            set.toList()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("All"))

    val filteredEntries: StateFlow<List<VocabEntry>> = combine(
        allEntries,
        selectedCategory,
        selectedSubCategory,
        selectedStudyTag,
        searchQuery
    ) { entries, category, subCategory, tag, query ->
        entries.filter { entry ->
            val matchesCategory = (category == "All" || entry.category == category)
            val matchesSubCategory = (subCategory == "All" || entry.subCategory == subCategory)
            val matchesTag = (tag == "All" || entry.studyTag == tag)
            val matchesQuery = query.isEmpty() ||
                    entry.kanjiWord.contains(query, ignoreCase = true) ||
                    entry.furiganaReading.contains(query, ignoreCase = true) ||
                    entry.meaning.contains(query, ignoreCase = true) ||
                    entry.category.contains(query, ignoreCase = true) ||
                    entry.subCategory.contains(query, ignoreCase = true) ||
                    entry.studyTag.contains(query, ignoreCase = true)
            matchesCategory && matchesSubCategory && matchesTag && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSelectedSubCategory(subCategory: String) {
        _selectedSubCategory.value = subCategory
    }

    fun setSelectedStudyTag(tag: String) {
        _selectedStudyTag.value = tag
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addVocabEntry(
        kanjiWord: String,
        furiganaReading: String,
        meaning: String,
        category: String,
        subCategory: String = "",
        studyTag: String,
        exampleSentence: String = ""
    ) {
        viewModelScope.launch {
            val entry = VocabEntry(
                kanjiWord = kanjiWord.trim(),
                furiganaReading = furiganaReading.trim(),
                meaning = meaning.trim(),
                category = category.trim().ifEmpty { "人の性格や個性などを表す言葉" },
                subCategory = subCategory.trim(),
                studyTag = studyTag.trim().ifEmpty { "第1週・1日目" },
                exampleSentence = exampleSentence.trim()
            )
            repository.insertEntry(entry)
        }
    }

    fun updateVocabEntry(entry: VocabEntry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
        }
    }

    fun toggleMasteredStatus(entry: VocabEntry) {
        viewModelScope.launch {
            repository.updateEntry(entry.copy(isMastered = !entry.isMastered))
        }
    }

    fun toggleStarredStatus(entry: VocabEntry) {
        viewModelScope.launch {
            repository.updateEntry(entry.copy(isStarred = !entry.isStarred))
        }
    }

    fun deleteVocabEntry(entry: VocabEntry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }
}
