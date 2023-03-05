package com.example.vkaudiorecorder.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "record_table")
data class RecordRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val date: Long,
    val duration: Long,
    val filePath: String
)
