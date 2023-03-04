package com.example.vkaudiorecorder.ui

import com.example.vkaudiorecorder.ui.model.Record
import java.util.*

val MOCKED_RECORD_LIST = listOf(
    Record(
        title = "Мальчик спел очень красиво Пирдуху Бабангиды",
        date = Date().time,
        duration = 2400,
        filePath = ""
    ),
    Record(
        title = "Пересказываю Войну и мир",
        date = 1234321231231,
        duration = 1234,
        filePath = ""
    ),
    Record(
        title = "Рыгаю",
        date = Date().time - 86400000,
        duration = 1234,
        filePath = ""
    )
)
