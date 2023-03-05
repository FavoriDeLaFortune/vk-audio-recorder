package com.example.vkaudiorecorder.ui.stateholders


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkaudiorecorder.data.local.RecordLoadState
import com.example.vkaudiorecorder.data.local.repository.LocalRecordRepository
import com.example.vkaudiorecorder.ui.model.Record
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecordViewModel(
    private val localRecordRepository: LocalRecordRepository
) : ViewModel() {

    private val _recordsStateFlow = MutableStateFlow<RecordLoadState>(RecordLoadState.Loaded(emptyList()))
    val recordsStateFlow: StateFlow<RecordLoadState> = _recordsStateFlow

    init {
        viewModelScope.launch {
            localRecordRepository.fetchRecords().collect() {
                _recordsStateFlow.value = it
            }
        }
    }

    fun refreshRecords() {
        viewModelScope.launch {
            localRecordRepository.fetchRecords().collect() {
                _recordsStateFlow.value = it
            }
        }
    }

    fun insertRecord(record: Record) {
        viewModelScope.launch {
            localRecordRepository.insertRecord(record = record)
        }
    }
}