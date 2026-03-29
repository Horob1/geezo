package com.horob1.geezo.api_debug.di

import com.horob1.geezo.api_debug.presentation.detail.ApiDebugDetailViewModel
import com.horob1.geezo.api_debug.presentation.list.ApiDebugViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val apiDebugModule = module {
    viewModel {
        ApiDebugViewModel(
            networkLogRepository = get()
        )
    }

    viewModel {
        ApiDebugDetailViewModel(
            networkLogRepository = get()
        )
    }
}
