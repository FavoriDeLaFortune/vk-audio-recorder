package com.example.vkaudiorecorder.di

import com.example.vkaudiorecorder.ui.stateholders.RecordViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RecordViewModel(get()) }
}