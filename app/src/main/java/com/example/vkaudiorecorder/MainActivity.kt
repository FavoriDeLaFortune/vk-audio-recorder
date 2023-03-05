package com.example.vkaudiorecorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.vkaudiorecorder.player.RecordAudioPlayer
import com.example.vkaudiorecorder.recorder.RecordAudioRecorder
import com.example.vkaudiorecorder.ui.MainScreen
import com.example.vkaudiorecorder.ui.theme.VKAudioRecorderTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    private val recorder by lazy { RecordAudioRecorder(applicationContext) }
    private val player by lazy { RecordAudioPlayer(applicationContext) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            0
        )
        setContent {
            VKAudioRecorderTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        applicationContext = applicationContext,
                        recorder = recorder,
                        player = player
                    )
                }
            }
        }
    }
}