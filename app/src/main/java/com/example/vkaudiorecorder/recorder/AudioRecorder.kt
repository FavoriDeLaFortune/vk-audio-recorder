package com.example.vkaudiorecorder.recorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}