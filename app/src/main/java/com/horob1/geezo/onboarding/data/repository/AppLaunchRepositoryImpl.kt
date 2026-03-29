package com.horob1.geezo.onboarding.data.repository

import com.horob1.geezo.onboarding.data.local.data_store.AppLaunchLocalDataSource
import com.horob1.geezo.onboarding.domain.repository.AppLaunchRepository
import kotlinx.coroutines.flow.Flow

class AppLaunchRepositoryImpl(
    private val localDataSource: AppLaunchLocalDataSource
) : AppLaunchRepository {

    override fun isFirstRun(): Flow<Boolean> {
        return localDataSource.isFirstRun()
    }

    override suspend fun setFirstRunCompleted() {
        localDataSource.setFirstRunCompleted()
    }
}

