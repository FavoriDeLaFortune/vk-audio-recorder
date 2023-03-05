package com.example.vkaudiorecorder.player

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
    fun pause()
    fun resume()
    fun isPlaying() : Boolean
    fun setOnComplitionListener(onComplete: () -> Unit)
}