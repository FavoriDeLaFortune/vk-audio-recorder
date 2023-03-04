package com.example.vkaudiorecorder

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vkaudiorecorder.ui.MOCKED_RECORD_LIST
import com.example.vkaudiorecorder.ui.mappers.dateToString
import com.example.vkaudiorecorder.ui.mappers.durationToString
import com.example.vkaudiorecorder.ui.model.Record
import com.example.vkaudiorecorder.ui.theme.VKAudioRecorderTheme
import kotlinx.coroutines.delay
import kotlin.math.sin

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
                RecordItem(record, interactions)
            }
        }
    }
    RecordSurface(interactions = interactions)
    RecordSaveCard(interactions = interactions)
    Column(horizontalAlignment = CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        RecordButton(interactionSource, interactions)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordSaveCard(interactions: SnapshotStateList<Interaction>) {
    AnimatedVisibility(
        visible = interactions.lastOrNull() is PressInteraction.Release,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Center) {
            Card(
                modifier = Modifier.size(width = 292.dp, height = 192.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(4.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                val textState = remember { mutableStateOf(TextFieldValue()) }
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Название записи",
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
                    Row(verticalAlignment = CenterVertically, modifier = Modifier.weight(0.3f)) {
                        Button(
                            onClick = { interactions.removeLast() },
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.5f),
                        ) {
                            Text(
                                text = "Отмена",
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
                            onClick = { interactions.removeLast() },
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.5f),
                        ) {
                            Text(
                                text = "Сохранить",
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
    if (interactions.lastOrNull() is PressInteraction.Release || interactions.lastOrNull() is PressInteraction.Cancel) {
        ticks = 0
    }
}

@Composable
fun RecordButton(
    interactionSource: MutableInteractionSource,
    interactions: SnapshotStateList<Interaction>
) {
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
        Icon(imageVector = Icons.Filled.Mic, contentDescription = null, Modifier.fillMaxSize())
    }
}

@Composable
fun RecordItem(record: Record, interactions: SnapshotStateList<Interaction>) {
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
                    .align(CenterVertically)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodySmall
            )
            PlayButton(
                modifier = Modifier
                    .size(32.dp)
                    .align(CenterVertically),
                interactions
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
fun PlayButton(modifier: Modifier, interactions: SnapshotStateList<Interaction>) {
    val isPlaying = remember { mutableStateOf(false) }
    Button(
        modifier = modifier,
        shape = CircleShape,
        contentPadding = PaddingValues(4.dp),
        onClick = { isPlaying.value = !isPlaying.value },
        enabled = interactions.lastOrNull() is PressInteraction.Press || interactions.lastOrNull() == null || interactions.lastOrNull() is PressInteraction.Cancel
    ) {
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