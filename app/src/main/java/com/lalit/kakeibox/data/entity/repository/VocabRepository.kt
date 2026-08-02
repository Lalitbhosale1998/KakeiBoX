package com.personal.kakeibox.data.entity.repository

import com.personal.kakeibox.data.dao.VocabDao
import com.personal.kakeibox.data.entity.VocabEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabRepository @Inject constructor(
    private val vocabDao: VocabDao
) {
    val allEntries: Flow<List<VocabEntry>> = vocabDao.getAllEntries()

    suspend fun insertEntry(entry: VocabEntry) {
        vocabDao.insertEntry(entry)
    }

    suspend fun updateEntry(entry: VocabEntry) {
        vocabDao.updateEntry(entry)
    }

    suspend fun deleteEntry(entry: VocabEntry) {
        vocabDao.deleteEntry(entry)
    }

    suspend fun deleteEntryById(id: Int) {
        vocabDao.deleteEntryById(id)
    }

    suspend fun getEntryCount(): Int = vocabDao.getEntryCount()
}
