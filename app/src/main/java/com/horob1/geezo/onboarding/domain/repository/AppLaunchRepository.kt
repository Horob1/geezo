package com.horob1.geezo.onboarding.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppLaunchRepository {
    fun isFirstRun(): Flow<Boolean>
    suspend fun setFirstRunCompleted()
}

