package com.example.vkaudiorecorder.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [RecordRoomEntity::class], version = 1)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun recordDao() : RecordDao
}