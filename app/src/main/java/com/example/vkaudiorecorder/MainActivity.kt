package com.example.vkaudiorecorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    color = MaterialTheme.colorScheme.primary
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
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
        Spacer(modifier = Modifier.weight(1f))
        RecordButton()
    }
}

@Composable
fun RecordButton() {
    Surface(
        modifier = Modifier
            .padding(36.dp)
            .size(60.dp)
            .shadow(8.dp, shape = CircleShape),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
    ) {
        Icon(imageVector = Icons.Filled.Mic, contentDescription = null, Modifier.fillMaxSize(0.5f))
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
                    text = record.dateToString(),
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = record.durationToString(),
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            PlayButton(modifier = Modifier
                .size(32.dp)
                .align(CenterVertically)
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
       /* ConstraintLayout {
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
            PlayButton(modifier = Modifier
                .constrainAs(playButton) {
                    end.linkTo(parent.end, margin = 8.dp)
                    start.linkTo(duration.end, margin = 4.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(36.dp))
        }
*/

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayButton(modifier: Modifier) {
    val isPlaying = remember { mutableStateOf(false) }
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
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