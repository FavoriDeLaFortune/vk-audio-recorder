package com.example.vkaudiorecorder.ui.mappers

import android.text.format.DateFormat
import com.example.vkaudiorecorder.ui.TODAY
import com.example.vkaudiorecorder.ui.YESTERDAY
import java.util.*

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
    val duration = currentCal.time.time - cal.time.time
    return if (duration < 86400000) {
        TODAY
    } else if (duration in 86400000..2*86400000) {
        YESTERDAY
    } else {
        "${DateFormat.format("dd.MM.yyyy", cal)}"
    }
}