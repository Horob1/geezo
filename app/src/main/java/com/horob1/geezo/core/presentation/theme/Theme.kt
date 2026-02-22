package com.horob1.geezo.core.presentation.theme


import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

// =========================
// Geezo color scheme
// =========================
private val GeezoColorScheme = darkColorScheme(
    background = GeezoColor.Background,
    surface = GeezoColor.Surface,
    primary = GeezoColor.Primary,
    onPrimary = GeezoColor.OnPrimary,
    onSurface = GeezoColor.OnSurface,
    onBackground = GeezoColor.OnBackground
)

/**
 * Compose theme for Geezo app
 *
 * @created_by NguyenTheAnh on 2/21/2026
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun GeezoTheme(
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val windowSize = if (context is Activity) {
        calculateWindowSizeClass(context)
    } else {
        WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))
    }

    val appDimens = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> CompactDimen
        WindowWidthSizeClass.Medium -> MediumDimen
        WindowWidthSizeClass.Expanded -> ExpandedDimen
        else -> CompactDimen
    }

    CompositionLocalProvider(
        LocalAppDimens provides appDimens
    ) {
        MaterialTheme(
            colorScheme = GeezoColorScheme,
            typography = AppTypography,
            content = content
        )
    }
}