package com.example.vkaudiorecorder.data.local.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RecordDao {

    @Query("SELECT * FROM record_table")
    suspend fun fetchRecords(): List<RecordRoomEntity>
}