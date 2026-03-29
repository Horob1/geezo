package com.horob1.geezo.core.di

import com.horob1.geezo.core.data.local.NetworkLogLocalDataSource
import org.koin.dsl.module

val dataModule = module {
    single { NetworkLogLocalDataSource(networkLogDao = get()) }
}
