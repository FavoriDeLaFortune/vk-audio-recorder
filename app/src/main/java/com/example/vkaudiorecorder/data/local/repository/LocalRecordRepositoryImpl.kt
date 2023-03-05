package com.example.vkaudiorecorder.data.local.repository

import com.example.vkaudiorecorder.data.local.RecordLoadState
import com.example.vkaudiorecorder.data.local.database.RecordDatabase
import com.example.vkaudiorecorder.data.local.database.RecordRoomEntity
import com.example.vkaudiorecorder.ui.model.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class LocalRecordRepositoryImpl(private val recordDatabase: RecordDatabase): LocalRecordRepository {

    override suspend fun fetchRecords(): Flow<RecordLoadState> = withContext(Dispatchers.IO) {
        flow {
            try {
                val list = recordDatabase.recordDao().fetchRecords()
                emit(RecordLoadState.Loaded(list.map { it.toDomainModel() }))
            } catch (_: Exception) {
                emit(RecordLoadState.Error)
            }
        }
    }

    override suspend fun insertRecord(record: Record) = withContext(Dispatchers.IO){
        recordDatabase.recordDao().insertRecord(record = record.toDatabaseModel())
    }

    private fun RecordRoomEntity.toDomainModel() = Record(
        id = this.id,
        title = this.title,
        date = this.date,
        duration = this.duration,
        filePath = this.filePath
    )

    private fun Record.toDatabaseModel() = RecordRoomEntity(
        id = this.id,
        title = this.title,
        date = this.date,
        duration = this.duration,
        filePath = this.filePath
    )
}