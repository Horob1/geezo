package com.horob1.geezo.api_debug.navigation

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
import androidx.navigation.toRoute
import com.horob1.geezo.api_debug.presentation.detail.ApiDebugDetailRoute
import com.horob1.geezo.api_debug.presentation.list.ApiDebugRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class ApiDebugRoutes {
    @Serializable
    data object List : ApiDebugRoutes()

    @Serializable
    data class Detail(val logId: Long) : ApiDebugRoutes()
}

@Composable
fun ApiDebugNavHost(
    onExitModule: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ApiDebugRoutes.List,
        modifier = modifier
    ) {
        composable<ApiDebugRoutes.List>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS))
            }
        ) {
            ApiDebugRoute(
                onBack = onExitModule,
                onOpenDetail = { logId ->
                    navController.navigate(ApiDebugRoutes.Detail(logId))
                }
            )
        }

        composable<ApiDebugRoutes.Detail>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(API_DEBUG_NAV_ANIMATION_DURATION_MS))
            }
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ApiDebugRoutes.Detail>()
            ApiDebugDetailRoute(
                logId = route.logId,
                onBack = {
                    if (!navController.popBackStack()) {
                        onExitModule()
                    }
                }
            )
        }
    }
}

private const val API_DEBUG_NAV_ANIMATION_DURATION_MS = 220

