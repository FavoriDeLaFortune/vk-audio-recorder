package com.example.vkaudiorecorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
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
        LazyColumn(modifier = Modifier.align(CenterHorizontally), contentPadding = PaddingValues(12.dp)) {
            items(MOCKED_RECORD_LIST) { record ->
                RecordItem(record)
            }
        }
    }
}

@Composable
fun RecordItem(record: Record) {
    Card {
        Row {
            Column {
                Box(modifier = Modifier.width(160.dp)) {
                    Text(text = record.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Text(text = record.dateToString())
            }
            Row {
                Text(text = record.durationToString())
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VKAudioRecorderTheme {
        MainScreen()
    }
}