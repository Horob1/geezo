package com.horob1.geezo.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horob1.geezo.onboarding.domain.repository.AppLaunchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val appLaunchRepository: AppLaunchRepository
) : ViewModel() {

    fun finishOnboarding() {
        viewModelScope.launch(Dispatchers.IO) {
            appLaunchRepository.setFirstRunCompleted()
        }
    }

}

