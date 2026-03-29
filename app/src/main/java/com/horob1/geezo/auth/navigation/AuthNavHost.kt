package com.horob1.geezo.auth.navigation

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
import com.horob1.geezo.auth.presentation.AuthForgotPasswordRoute
import com.horob1.geezo.auth.presentation.AuthOtpRoute
import com.horob1.geezo.auth.presentation.AuthResetPasswordRoute
import com.horob1.geezo.auth.presentation.AuthSignInRoute
import com.horob1.geezo.auth.presentation.AuthSignUpRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoutes {
    @Serializable
    data object SignIn : AuthRoutes()

    @Serializable
    data object SignUp : AuthRoutes()

    @Serializable
    data object ForgotPassword : AuthRoutes()

    @Serializable
    data class VerifyOtp(val email: String) : AuthRoutes()

    @Serializable
    data class ResetPassword(val email: String) : AuthRoutes()
}

@Composable
fun AuthNavHost(
    onExitModule: () -> Unit,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.SignIn,
        modifier = modifier
    ) {
        composable<AuthRoutes.SignIn>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(AUTH_NAV_ANIMATION_DURATION_MS)
                ) + fadeIn(animationSpec = tween(AUTH_NAV_ANIMATION_DURATION_MS))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth / 3 },
                    animationSpec = tween(AUTH_NAV_ANIMATION_DURATION_MS)
                ) + fadeOut(animationSpec = tween(AUTH_NAV_ANIMATION_DURATION_MS))
            }
        ) {
            AuthSignInRoute(
                onBack = onExitModule,
                onOpenSignUp = { navController.navigate(AuthRoutes.SignUp) },
                onOpenForgotPassword = { navController.navigate(AuthRoutes.ForgotPassword) },
                onSignInSuccess = onAuthSuccess
            )
        }

        composable<AuthRoutes.SignUp> {
            AuthSignUpRoute(
                onBack = { navController.popBackStack() },
                onSignedUp = onAuthSuccess
            )
        }

        composable<AuthRoutes.ForgotPassword> {
            AuthForgotPasswordRoute(
                onBack = { navController.popBackStack() },
                onContinue = { email -> navController.navigate(AuthRoutes.VerifyOtp(email)) }
            )
        }

        composable<AuthRoutes.VerifyOtp> { backStackEntry ->
            val route = backStackEntry.toRoute<AuthRoutes.VerifyOtp>()
            AuthOtpRoute(
                email = route.email,
                onBack = { navController.popBackStack() },
                onVerified = { navController.navigate(AuthRoutes.ResetPassword(route.email)) }
            )
        }

        composable<AuthRoutes.ResetPassword> { backStackEntry ->
            val route = backStackEntry.toRoute<AuthRoutes.ResetPassword>()
            AuthResetPasswordRoute(
                email = route.email,
                onBack = { navController.popBackStack() },
                onDone = onAuthSuccess
            )
        }
    }
}

private const val AUTH_NAV_ANIMATION_DURATION_MS = 220

