package com.example.vkaudiorecorder.data.local.database

import androidx.room.Dao
import androidx.room.Query
import com.example.vkaudiorecorder.ui.model.Record

@Dao
interface RecordDao {

    @Query("SELECT * FROM record_table")
    suspend fun fetchRecords(): List<Record>
}