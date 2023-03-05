package com.example.vkaudiorecorder.data.local.repository

import com.example.vkaudiorecorder.data.local.RecordLoadState
import com.example.vkaudiorecorder.ui.model.Record
import kotlinx.coroutines.flow.Flow

interface LocalRecordRepository {

    suspend fun fetchRecords(): Flow<RecordLoadState>

    suspend fun insertRecord(record: Record)
}