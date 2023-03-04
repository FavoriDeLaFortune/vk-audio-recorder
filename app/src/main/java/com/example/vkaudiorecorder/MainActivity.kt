package com.example.vkaudiorecorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vkaudiorecorder.ui.MOCKED_RECORD_LIST
import com.example.vkaudiorecorder.ui.mappers.dateToString
import com.example.vkaudiorecorder.ui.mappers.durationToString
import com.example.vkaudiorecorder.ui.model.Record
import com.example.vkaudiorecorder.ui.theme.VKAudioRecorderTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKAudioRecorderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val interactions = remember { mutableStateListOf<Interaction>() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interactions.isNotEmpty()) {
                interactions.removeLast()
            }
            interactions.add(interaction)
        }
    }
    Column(horizontalAlignment = CenterHorizontally) {
        Text(
            text = "Ваши записи", modifier = Modifier
                .align(Start)
                .padding(start = 16.dp, top = 8.dp, bottom = 24.dp),
            style = MaterialTheme.typography.labelLarge
        )
        LazyColumn(
            modifier = Modifier.align(CenterHorizontally),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(MOCKED_RECORD_LIST) { record ->
                RecordItem(record)
            }
        }
    }
    RecordSurface(interactions = interactions)
    Column(horizontalAlignment = CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        RecordButton(interactionSource, interactions.any { it is PressInteraction.Press })
    }
}

@Composable
fun RecordSurface(interactions: SnapshotStateList<Interaction>) {
    var ticks by remember { mutableStateOf(0L) }
    AnimatedVisibility(
        visible = interactions.lastOrNull() is PressInteraction.Press,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        )
                    )
                ),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.55f))
            Column(modifier = Modifier.weight(0.45f), horizontalAlignment = CenterHorizontally) {
                Text(
                    text = "Идёт запись",
                    style = MaterialTheme.typography.labelMedium,
                )
                Spacer(modifier = Modifier.size(24.dp))
                Text(text = durationToString(ticks), style = MaterialTheme.typography.labelSmall)
            }
            LaunchedEffect(ticks) {
                while (true) {
                    delay(1000)
                    ticks++
                }
            }
        }
    }
    if (interactions.lastOrNull() is PressInteraction.Release) {
        LaunchedEffect(ticks) {
            delay(250)
            ticks = 0
        }
    }
}

@Composable
fun RecordButton(interactionSource: MutableInteractionSource, isPressed: Boolean) {
    Button(
        modifier = Modifier
            .padding(36.dp)
            .size(64.dp)
            .shadow(12.dp, shape = CircleShape),
        shape = CircleShape,
        elevation = ButtonDefaults.buttonElevation(8.dp),
        contentPadding = PaddingValues(12.dp),
        interactionSource = interactionSource,
        colors = if (isPressed) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(
            MaterialTheme.colorScheme.primary
        ),
        onClick = {}
    ) {
        Icon(imageVector = Icons.Filled.Mic, contentDescription = null, Modifier.fillMaxSize())
    }
}

@Composable
fun RecordItem(record: Record) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row {
            Column(modifier = Modifier.weight(0.4f)) {
                Text(
                    text = record.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = dateToString(record.date),
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.weight(0.1f))
            Text(
                text = durationToString(record.duration),
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            PlayButton(
                modifier = Modifier
                    .size(32.dp)
                    .align(CenterVertically)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
fun PlayButton(modifier: Modifier) {
    val isPlaying = remember { mutableStateOf(false) }
    Button(
        modifier = modifier,
        shape = CircleShape,
        contentPadding = PaddingValues(4.dp),
        onClick = { isPlaying.value = !isPlaying.value }) {
        Icon(
            imageVector = if (isPlaying.value) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VKAudioRecorderTheme {
        MainScreen()
    }
}