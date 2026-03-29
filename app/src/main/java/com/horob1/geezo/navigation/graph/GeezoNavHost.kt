package com.horob1.geezo.navigation.graph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.horob1.geezo.api_debug.navigation.ApiDebugNavHost
import com.horob1.geezo.main.MainScreen
import com.horob1.geezo.navigation.Routes
import com.horob1.geezo.onboarding.navigation.OnboardingNavHost

@Composable
fun GeezoNavHost(
    navController: NavHostController,
    startDestination: Routes,
    onOnboardingCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Routes.OnBoarding>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            }
        ) {
            OnboardingNavHost(
                onFinishModule = {
                    onOnboardingCompleted()
                    navController.navigate(Routes.Main) {
                        popUpTo(Routes.OnBoarding) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Routes.Main>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            }
        ) {
            MainScreen()
        }

        composable<Routes.ApiDebug>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(NAV_ANIMATION_DURATION_MS))
            }
        ) {
            ApiDebugNavHost(
                onExitModule = { navController.popBackStack() },
                modifier = Modifier
            )
        }
    }
}

private const val NAV_ANIMATION_DURATION_MS = 220
