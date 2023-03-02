package com.example.vkaudiorecorder.data.local.repository

import com.example.vkaudiorecorder.data.local.RecordLoadState
import com.example.vkaudiorecorder.data.local.database.RecordDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class LocalRecordRepositoryImpl(private val recordDatabase: RecordDatabase): LocalRecordRepository {

    override suspend fun fetchRecords(): Flow<RecordLoadState> = withContext(Dispatchers.IO) {
        flow {
            try {
                val list = recordDatabase.recordDao().fetchRecords()
                emit(RecordLoadState.Loaded(list))
            } catch (_: Exception) {
                emit(RecordLoadState.Error)
            }
        }
    }
}