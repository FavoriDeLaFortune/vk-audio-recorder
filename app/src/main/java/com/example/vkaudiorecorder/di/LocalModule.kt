package com.example.vkaudiorecorder.di

import android.content.Context
import androidx.room.Room
import com.example.vkaudiorecorder.data.local.database.RecordDatabase
import com.example.vkaudiorecorder.data.local.repository.LocalRecordRepository
import com.example.vkaudiorecorder.data.local.repository.LocalRecordRepositoryImpl
import org.koin.dsl.module

val localModule = module {
    single { provideDatabase(get()) }
    single<LocalRecordRepository> { LocalRecordRepositoryImpl(get()) }
}

fun provideDatabase(applicationContext: Context): RecordDatabase =
    Room.databaseBuilder(applicationContext, RecordDatabase::class.java, "record_table").build()