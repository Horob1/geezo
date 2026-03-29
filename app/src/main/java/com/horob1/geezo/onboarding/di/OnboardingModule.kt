package com.horob1.geezo.onboarding.di

import com.horob1.geezo.onboarding.data.local.data_store.AppLaunchLocalDataSource
import com.horob1.geezo.onboarding.data.repository.AppLaunchRepositoryImpl
import com.horob1.geezo.onboarding.domain.repository.AppLaunchRepository
import com.horob1.geezo.onboarding.presentation.OnboardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {
    single { AppLaunchLocalDataSource(context = androidContext()) }
    single<AppLaunchRepository> { AppLaunchRepositoryImpl(localDataSource = get()) }
    viewModel {
        OnboardingViewModel(
            appLaunchRepository = get()
        )
    }
}
