package com.example.vkaudiorecorder.ui.model


data class Record(
    val id: Int = 0,
    val title: String,
    val date: Long,
    val duration: Long,
    val filePath: String
)
