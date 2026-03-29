package com.horob1.geezo.onboarding.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.horob1.geezo.onboarding.presentation.OnboardingRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class OnboardingRoutes {
    @Serializable
    data object Welcome : OnboardingRoutes()
}

@Composable
fun OnboardingNavHost(
    onFinishModule: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = OnboardingRoutes.Welcome,
        modifier = modifier
    ) {
        composable<OnboardingRoutes.Welcome>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(ONBOARDING_NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(ONBOARDING_NAV_ANIMATION_DURATION_MS))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(ONBOARDING_NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(ONBOARDING_NAV_ANIMATION_DURATION_MS))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(ONBOARDING_NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(ONBOARDING_NAV_ANIMATION_DURATION_MS))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(ONBOARDING_NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(ONBOARDING_NAV_ANIMATION_DURATION_MS))
            }
        ) {
            OnboardingRoute(onFinish = onFinishModule)
        }
    }
}

private const val ONBOARDING_NAV_ANIMATION_DURATION_MS = 220

