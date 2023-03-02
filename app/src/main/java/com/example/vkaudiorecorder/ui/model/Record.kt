package com.example.vkaudiorecorder.ui.model

import java.util.Calendar
import java.util.Date
import android.text.format.DateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration.Companion.milliseconds

@Entity(tableName = "record_table")
data class Record(val title: String, val date: Date, val duration: Long, val filePath: String) {

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

    fun dateToString(): String {
        val recordCal = Calendar.getInstance()
        val currentCal = Calendar.getInstance()
        recordCal.time = date
        val str = if (isToday(recordCal, currentCal)) {
            "Сегодня"
        } else if (isYesterday(recordCal, currentCal)) {
            "Вчера"
        } else {
            "${DateFormat.format("dd.MM.yyyy", recordCal)}"
        }
        return str + " в ${DateFormat.format("HH:mm", recordCal)}"
    }

    fun durationToString() = String.format("%02d:%02d", duration / 60, duration % 60)

    private fun isToday(cal: Calendar, currentCal: Calendar): Boolean {
        val duration = (currentCal.timeInMillis - cal.timeInMillis).milliseconds
        return duration.inWholeHours < 24
    }

    private fun isYesterday(cal: Calendar, currentCal: Calendar): Boolean {
        val duration = (currentCal.timeInMillis - cal.timeInMillis).milliseconds
        return duration.inWholeHours in 24..47
    }
}
