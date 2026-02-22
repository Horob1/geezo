package com.horob1.geezo.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// =======================================================
// AppDimen - Design System Dimension
// Base = Compact (1.0f scale)
// =======================================================

data class AppDimen(

    // ==================== Spacing (4dp grid) ====================
    val s0: Dp = 0.dp,
    val s1: Dp = 4.dp,
    val s2: Dp = 8.dp,
    val s3: Dp = 12.dp,
    val s4: Dp = 16.dp,
    val s5: Dp = 20.dp,
    val s6: Dp = 24.dp,
    val s7: Dp = 32.dp,
    val s8: Dp = 40.dp,
    val s9: Dp = 48.dp,
    val s10: Dp = 64.dp,

    // ==================== Radius ====================
    val r0: Dp = 0.dp,
    val r1: Dp = 4.dp,
    val r2: Dp = 8.dp,
    val r3: Dp = 12.dp,
    val r4: Dp = 16.dp,
    val r5: Dp = 24.dp,
    val rFull: Dp = 1000.dp,

    // ==================== Icon ====================
    val iconXS: Dp = 16.dp,
    val iconSM: Dp = 20.dp,
    val iconMD: Dp = 24.dp,
    val iconLG: Dp = 32.dp,
    val iconXL: Dp = 40.dp,

    // ==================== Avatar ====================
    val avatarSM: Dp = 32.dp,
    val avatarMD: Dp = 40.dp,
    val avatarLG: Dp = 56.dp,
    val avatarXL: Dp = 72.dp,

    // ==================== Buttons ====================
    val buttonSM: Dp = 36.dp,
    val buttonMD: Dp = 44.dp,
    val buttonLG: Dp = 52.dp,

    // ==================== Inputs ====================
    val inputHeight: Dp = 56.dp,

    // ==================== App Bars ====================
    val topBarHeight: Dp = 56.dp,
    val bottomBarHeight: Dp = 80.dp,

    // ==================== Card ====================
    val cardElevation: Dp = 4.dp,
    val cardElevationHigh: Dp = 8.dp,

    // ==================== Divider ====================
    val divider: Dp = 1.dp,

    // ==================== Touch Target ====================
    val touchMin: Dp = 48.dp,
)

// =======================================================
// Scale Extensions
// =======================================================

private fun Dp.scale(factor: Float): Dp = (value * factor).dp

fun AppDimen.scale(factor: Float) = AppDimen(

    s0 = s0.scale(factor),
    s1 = s1.scale(factor),
    s2 = s2.scale(factor),
    s3 = s3.scale(factor),
    s4 = s4.scale(factor),
    s5 = s5.scale(factor),
    s6 = s6.scale(factor),
    s7 = s7.scale(factor),
    s8 = s8.scale(factor),
    s9 = s9.scale(factor),
    s10 = s10.scale(factor),

    r0 = r0.scale(factor),
    r1 = r1.scale(factor),
    r2 = r2.scale(factor),
    r3 = r3.scale(factor),
    r4 = r4.scale(factor),
    r5 = r5.scale(factor),
    rFull = rFull,

    iconXS = iconXS.scale(factor),
    iconSM = iconSM.scale(factor),
    iconMD = iconMD.scale(factor),
    iconLG = iconLG.scale(factor),
    iconXL = iconXL.scale(factor),

    avatarSM = avatarSM.scale(factor),
    avatarMD = avatarMD.scale(factor),
    avatarLG = avatarLG.scale(factor),
    avatarXL = avatarXL.scale(factor),

    buttonSM = buttonSM.scale(factor),
    buttonMD = buttonMD.scale(factor),
    buttonLG = buttonLG.scale(factor),

    inputHeight = inputHeight.scale(factor),

    topBarHeight = topBarHeight.scale(factor),
    bottomBarHeight = bottomBarHeight.scale(factor),

    cardElevation = cardElevation,
    cardElevationHigh = cardElevationHigh,

    divider = divider,
    touchMin = touchMin.scale(factor),
)

// =======================================================
// Dimension Presets
// =======================================================

private val BaseDimen = AppDimen()
val CompactDimen = BaseDimen
val MediumDimen = BaseDimen.scale(1.15f)
val ExpandedDimen = BaseDimen.scale(1.30f)

// =======================================================
// CompositionLocal
// =======================================================

val LocalAppDimens = staticCompositionLocalOf {
    CompactDimen
}

val MaterialTheme.dimens
    @Composable
    @ReadOnlyComposable
    get() = LocalAppDimens.current
