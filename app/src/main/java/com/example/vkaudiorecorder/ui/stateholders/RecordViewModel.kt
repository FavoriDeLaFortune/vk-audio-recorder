package com.example.vkaudiorecorder.ui.stateholders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vkaudiorecorder.data.local.RecordLoadState
import com.example.vkaudiorecorder.data.local.repository.LocalRecordRepository
import kotlinx.coroutines.flow.Flow

class RecordViewModel(
    val handle: SavedStateHandle,
    private val localRecordRepository: LocalRecordRepository
) : ViewModel() {

    suspend fun allRecordsFlow(): Flow<RecordLoadState> = localRecordRepository.fetchRecords()
}