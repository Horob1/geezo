package com.horob1.geezo.core.di

import com.horob1.geezo.BuildConfig
import com.horob1.geezo.core.data.remote.api.KtorClientProvider
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single {
        KtorClientProvider(
            networkLogRepository = get(),
            environment = if (BuildConfig.DEBUG) "dev" else "prod",
            tag = "ktor",
            isMock = false
        )
    }

    single<HttpClient> { get<KtorClientProvider>().client }
}

