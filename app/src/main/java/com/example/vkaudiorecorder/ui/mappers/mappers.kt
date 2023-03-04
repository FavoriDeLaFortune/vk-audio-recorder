package com.example.vkaudiorecorder.ui.mappers

import android.text.format.DateFormat
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

fun durationToString(duration: Long) = String.format("%02d:%02d", duration / 60, duration % 60)

fun dateToString(date: Long): String {
    val recordCal = Calendar.getInstance()
    val currentCal = Calendar.getInstance()
    recordCal.time = Date(date)
    return isTodayOrYesterday(recordCal, currentCal) + " в ${
        DateFormat.format(
            "HH:mm",
            recordCal
        )
    }"
}


private fun isTodayOrYesterday(cal: Calendar, currentCal: Calendar): String {
    val duration = (currentCal.timeInMillis - cal.timeInMillis).milliseconds
    return if (duration.inWholeHours < 24) {
        "Сегодня"
    } else if (duration.inWholeHours in 24..47) {
        "Вчера"
    } else {
        "${DateFormat.format("dd.MM.yyyy", cal)}"
    }
}