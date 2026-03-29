package com.horob1.geezo.core.di

import com.horob1.geezo.core.data.repository.NetworkLogRepositoryImpl
import com.horob1.geezo.core.domain.repository.NetworkLogRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NetworkLogRepository> { NetworkLogRepositoryImpl(localDataSource = get()) }
}
