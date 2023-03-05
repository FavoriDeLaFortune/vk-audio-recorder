package com.example.vkaudiorecorder.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vkaudiorecorder.*
import com.example.vkaudiorecorder.data.local.RecordLoadState
import com.example.vkaudiorecorder.player.RecordAudioPlayer
import com.example.vkaudiorecorder.recorder.RecordAudioRecorder
import com.example.vkaudiorecorder.ui.mappers.dateToString
import com.example.vkaudiorecorder.ui.mappers.durationToString
import com.example.vkaudiorecorder.ui.model.Record
import com.example.vkaudiorecorder.ui.stateholders.RecordViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.util.*

@Composable
fun MainScreen(
    recordViewModel: RecordViewModel = koinViewModel(),
    applicationContext: Context,
    recorder: RecordAudioRecorder,
    player: RecordAudioPlayer
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val interactions = remember { mutableStateListOf<Interaction>() }
    val recordLoadState by recordViewModel.recordsStateFlow.collectAsStateWithLifecycle()
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interactions.isNotEmpty()) {
                interactions.removeLast()
            }
            interactions.add(interaction)
        }
    }
    MainColumn(
        recordLoadState = recordLoadState,
        player = player,
        interactions = interactions,
        applicationContext = applicationContext
    )
    var duration by remember { mutableStateOf(0L) }
    RecordSurface(interactions = interactions) { _duration -> duration = _duration }
    RecordSaveCard(
        viewModel = recordViewModel,
        interactions = interactions,
        duration = duration,
        recordLoadState = recordLoadState,
        applicationContext = applicationContext
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        RecordButton(
            interactionSource = interactionSource,
            interactions = interactions,
            applicationContext = applicationContext,
            recorder = recorder,
            player = player
        )
    }
}

@Composable
fun MainColumn(
    recordLoadState: RecordLoadState,
    player: RecordAudioPlayer,
    interactions: SnapshotStateList<Interaction>,
    applicationContext: Context
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = MAIN_TITLE, modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, top = 8.dp, bottom = 24.dp),
            style = MaterialTheme.typography.labelLarge
        )
        when (recordLoadState) {
            is RecordLoadState.Loaded -> {
                if (recordLoadState.list.isNotEmpty()) {
                    RecordsList(
                        recordLoadState = recordLoadState,
                        player = player,
                        interactions = interactions,
                        applicationContext = applicationContext
                    )
                } else {
                    Spacer(modifier = Modifier.weight(0.4f))
                    Text(
                        text = EMPTY_LIST_MESSAGE,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(0.6f)
                    )
                }
            }
            is RecordLoadState.Error -> {
                Spacer(modifier = Modifier.weight(0.4f))
                Text(
                    text = ERROR_MESSAGE,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(0.6f)
                )
            }
        }
    }
}

@Composable
fun RecordsList(
    recordLoadState: RecordLoadState,
    player: RecordAudioPlayer,
    interactions: SnapshotStateList<Interaction>,
    applicationContext: Context
) {
    var selectedRecord by remember { mutableStateOf<Record?>(null) }
    if (interactions.lastOrNull() is PressInteraction.Press) {
        selectedRecord = null
    }
    var isPlaying by remember { mutableStateOf(player.isPlaying()) }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items((recordLoadState as RecordLoadState.Loaded).list) { record ->
            player.setOnComplitionListener { isPlaying = player.isPlaying() }
            RecordItem(
                record = record,
                interactions = interactions,
                isSelected = record == selectedRecord,
                isPlaying = isPlaying,
                onClick = { isSelected ->
                    if (isSelected) {
                        if (player.isPlaying()) {
                            player.pause()
                        } else {
                            player.resume()
                        }
                    } else {
                        val audioFile =
                            File(applicationContext.filesDir, record.filePath)
                        player.playFile(audioFile)
                        selectedRecord = record
                    }
                    isPlaying = player.isPlaying()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordSaveCard(
    viewModel: RecordViewModel,
    interactions: SnapshotStateList<Interaction>,
    duration: Long,
    recordLoadState: RecordLoadState,
    applicationContext: Context
) {
    AnimatedVisibility(
        visible = interactions.lastOrNull() is PressInteraction.Release,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val itemsCount =
            if (recordLoadState is RecordLoadState.Loaded) recordLoadState.list.size else 0
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.size(width = 292.dp, height = 192.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(4.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                val textState = remember { mutableStateOf(TextFieldValue()) }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = RECORD_SAVE_CARD_TITLE,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                    TextField(
                        value = textState.value,
                        onValueChange = { textState.value = it },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                            .weight(0.4f)
                            .fillMaxSize(),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.labelSmall
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(0.3f)
                    ) {
                        Button(
                            onClick = { interactions.removeLast() },
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.5f),
                        ) {
                            Text(
                                text = DISMISS_BUTTON_TEXT,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(4.dp),
                                maxLines = 1
                            )
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                        Button(
                            onClick = {
                                viewModel.insertRecord(
                                    Record(
                                        title = textState.value.text,
                                        date = Date().time,
                                        duration = duration,
                                        filePath = "audio_${itemsCount + 1}.mp3"
                                    )
                                )
                                File(
                                    applicationContext.filesDir,
                                    CURRENT_RECORD_FILE_PATH
                                ).renameTo(
                                    File(
                                        applicationContext.filesDir,
                                        "audio_${itemsCount + 1}.mp3"
                                    )
                                )
                                interactions.removeLast()
                                viewModel.refreshRecords()
                            },
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.5f),
                        ) {
                            Text(
                                text = CONFIRM_BUTTON_TEXT,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(4.dp),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecordSurface(
    interactions: SnapshotStateList<Interaction>,
    onRelease: (duration: Long) -> Unit
) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var ticks by remember { mutableStateOf(0L) }
            Spacer(modifier = Modifier.weight(0.55f))
            Column(
                modifier = Modifier.weight(0.45f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = RECORD_SURFACE_TITLE,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(24.dp))
                Text(
                    text = durationToString(ticks),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
            LaunchedEffect(ticks) {
                while (true) {
                    delay(1000)
                    ticks++
                }
            }
            when (interactions.lastOrNull()) {
                is PressInteraction.Release -> {
                    onRelease(ticks)
                }
            }
        }
    }
}

@Composable
fun RecordButton(
    interactionSource: MutableInteractionSource,
    interactions: SnapshotStateList<Interaction>,
    applicationContext: Context,
    recorder: RecordAudioRecorder,
    player: RecordAudioPlayer
) {
    val audioFile = File(applicationContext.filesDir, CURRENT_RECORD_FILE_PATH)
    Button(
        modifier = Modifier
            .padding(36.dp)
            .size(64.dp),
        shape = CircleShape,
        elevation = ButtonDefaults.buttonElevation(8.dp),
        contentPadding = PaddingValues(12.dp),
        interactionSource = interactionSource,
        colors = if (interactions.lastOrNull() is PressInteraction.Press) ButtonDefaults.buttonColors(
            Color.Red
        ) else ButtonDefaults.buttonColors(
            MaterialTheme.colorScheme.primary
        ),
        enabled = interactions.lastOrNull() is PressInteraction.Press || interactions.lastOrNull() == null || interactions.lastOrNull() is PressInteraction.Cancel,
        onClick = {}
    ) {
        when (interactions.lastOrNull()) {
            is PressInteraction.Press -> {
                player.stop()
                recorder.start(audioFile)
            }
            is PressInteraction.Cancel -> {
                recorder.stop()
                audioFile.delete()
            }
            is PressInteraction.Release -> {
                recorder.stop()
            }
        }
        Icon(imageVector = Icons.Filled.Mic, contentDescription = null, Modifier.fillMaxSize())
    }
}

@Composable
fun RecordItem(
    record: Record,
    interactions: SnapshotStateList<Interaction>,
    isSelected: Boolean,
    isPlaying: Boolean,
    onClick: (isSelected: Boolean) -> Unit
) {
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
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.weight(0.1f))
            Text(
                text = durationToString(record.duration),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodySmall
            )
            PlayButton(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterVertically),
                interactions = interactions,
                isSelected = isSelected,
                isPlaying = isPlaying,
                onClick = onClick
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
fun PlayButton(
    modifier: Modifier,
    interactions: SnapshotStateList<Interaction>,
    isSelected: Boolean,
    isPlaying: Boolean,
    onClick: (isSelected: Boolean) -> Unit
) {
    Button(
        modifier = modifier,
        shape = CircleShape,
        contentPadding = PaddingValues(4.dp),
        onClick = { onClick(isSelected) },
        enabled = interactions.lastOrNull() is PressInteraction.Press || interactions.lastOrNull() == null || interactions.lastOrNull() is PressInteraction.Cancel
    ) {
        Icon(
            imageVector = if (isPlaying && isSelected) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = null,
        )
    }
}