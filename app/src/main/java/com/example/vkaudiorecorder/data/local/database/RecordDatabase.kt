package com.example.vkaudiorecorder.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vkaudiorecorder.ui.model.Record


@Database(entities = [Record::class], version = 1)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun recordDao() : RecordDao
}