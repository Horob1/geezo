package com.horob1.geezo.core.di

import com.horob1.geezo.api_debug.di.apiDebugModule
import com.horob1.geezo.onboarding.di.onboardingModule

val appModules = listOf(
    databaseModule,
    dataModule,
    repositoryModule,
    networkModule,
    apiDebugModule,
    onboardingModule
)
