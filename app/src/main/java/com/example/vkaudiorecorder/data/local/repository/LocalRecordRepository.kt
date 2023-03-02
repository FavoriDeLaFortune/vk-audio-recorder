package com.example.vkaudiorecorder.data.local.repository

import com.example.vkaudiorecorder.data.local.RecordLoadState
import kotlinx.coroutines.flow.Flow

interface LocalRecordRepository {

    suspend fun fetchRecords(): Flow<RecordLoadState>
}