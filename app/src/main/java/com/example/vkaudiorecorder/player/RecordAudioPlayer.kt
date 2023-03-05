package com.example.vkaudiorecorder.player

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class RecordAudioPlayer(private val context: Context) : AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        if (player != null && player!!.isPlaying) {
            stop()
        }
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

    override fun pause() {
        player?.pause()
    }

    override fun resume() {
        player?.start()
    }

    override fun isPlaying() = player?.isPlaying ?: false

    override fun setOnComplitionListener(onComplete: () -> Unit) {
        player?.setOnCompletionListener {
            onComplete()
        }
    }
}