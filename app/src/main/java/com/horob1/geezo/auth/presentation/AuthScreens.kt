package com.horob1.geezo.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.horob1.geezo.core.presentation.theme.dimens

@Composable
fun AuthSignInRoute(
    onBack: () -> Unit,
    onOpenSignUp: () -> Unit,
    onOpenForgotPassword: () -> Unit,
    onSignInSuccess: () -> Unit
) {
    AuthPlaceholderScreen(
        title = "Auth - Sign In",
        actions = listOf(
            "Sign In Success" to onSignInSuccess,
            "Open Sign Up" to onOpenSignUp,
            "Forgot Password" to onOpenForgotPassword,
            "Back" to onBack
        )
    )
}

@Composable
fun AuthSignUpRoute(
    onBack: () -> Unit,
    onSignedUp: () -> Unit
) {
    AuthPlaceholderScreen(
        title = "Auth - Sign Up",
        actions = listOf(
            "Complete Sign Up" to onSignedUp,
            "Back" to onBack
        )
    )
}

@Composable
fun AuthForgotPasswordRoute(
    onBack: () -> Unit,
    onContinue: (String) -> Unit
) {
    AuthPlaceholderScreen(
        title = "Auth - Forgot Password",
        actions = listOf(
            "Send OTP" to { onContinue("demo@geezo.app") },
            "Back" to onBack
        )
    )
}

@Composable
fun AuthOtpRoute(
    email: String,
    onBack: () -> Unit,
    onVerified: () -> Unit
) {
    AuthPlaceholderScreen(
        title = "Auth - Verify OTP",
        subtitle = "Email: $email",
        actions = listOf(
            "Verify" to onVerified,
            "Back" to onBack
        )
    )
}

@Composable
fun AuthResetPasswordRoute(
    email: String,
    onBack: () -> Unit,
    onDone: () -> Unit
) {
    AuthPlaceholderScreen(
        title = "Auth - Reset Password",
        subtitle = "Email: $email",
        actions = listOf(
            "Done" to onDone,
            "Back" to onBack
        )
    )
}

@Composable
private fun AuthPlaceholderScreen(
    title: String,
    subtitle: String? = null,
    actions: List<Pair<String, () -> Unit>>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimens.s4),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.s2,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        if (!subtitle.isNullOrBlank()) {
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }

        actions.forEachIndexed { index, (label, action) ->
            if (index == actions.lastIndex) {
                OutlinedButton(
                    onClick = action,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = label)
                }
            } else {
                Button(
                    onClick = action,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = label)
                }
            }
        }
    }
}

