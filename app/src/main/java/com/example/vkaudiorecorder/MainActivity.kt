package com.example.vkaudiorecorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.vkaudiorecorder.ui.MOCKED_RECORD_LIST
import com.example.vkaudiorecorder.ui.model.Record
import com.example.vkaudiorecorder.ui.theme.VKAudioRecorderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKAudioRecorderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column {
        Text(text = "Ваши записи")
        LazyColumn(
            modifier = Modifier.align(CenterHorizontally),
        ) {
            items(MOCKED_RECORD_LIST) { record ->
                RecordItem(record)
            }
        }
    }
}

@Composable
fun RecordItem(record: Record) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        ConstraintLayout {
            val (title, date, duration, playButton) = createRefs()

            Text(modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start, margin = 8.dp)
                end.linkTo(duration.start, margin = 8.dp)
                width = Dimension.fillToConstraints
            }, text = record.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(modifier = Modifier.constrainAs(date) {
                top.linkTo(title.bottom, margin = 4.dp)
                start.linkTo(parent.start, margin = 8.dp)
                bottom.linkTo(parent.bottom, margin = 8.dp)
            }, text = record.dateToString())
            Text(modifier = Modifier.constrainAs(duration) {
                start.linkTo(title.end)
                end.linkTo(playButton.start, margin = 8.dp)
                bottom.linkTo(parent.bottom)
                top.linkTo(parent.top)
            }, text = record.durationToString())
            PlayButton(modifier = Modifier.constrainAs(playButton) {
                end.linkTo(parent.end, margin = 8.dp)
                start.linkTo(duration.end, margin = 4.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }.size(48.dp))
        }


    }
}

@Composable
fun PlayButton(modifier: Modifier) {
    val isPlaying = remember { mutableStateOf(false) }
    Button(
        modifier = modifier,
        shape = CircleShape,
        onClick = { isPlaying.value = !isPlaying.value }) {
        Icon(
            imageVector = if (isPlaying.value) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = null
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