package com.example.vkaudiorecorder.data.local

import com.example.vkaudiorecorder.ui.model.Record

sealed class RecordLoadState {
    class Loaded(val list: List<Record>) : RecordLoadState()
    object Error : RecordLoadState()
}
