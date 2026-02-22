package com.horob1.geezo.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppFont = FontFamily.Default

val AppTypography = Typography(

    displayLarge = TextStyle(
        fontFamily = AppFont,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 56.sp
    ),

    displayMedium = TextStyle(
        fontFamily = AppFont,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 44.sp
    ),

    displaySmall = TextStyle(
        fontFamily = AppFont,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = AppFont,
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 36.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = AppFont,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 30.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = AppFont,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = AppFont,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),

    bodySmall = TextStyle(
        fontFamily = AppFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp
    ),

    labelLarge = TextStyle(
        fontFamily = AppFont,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp
    ),

    labelMedium = TextStyle(
        fontFamily = AppFont,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp
    ),

    labelSmall = TextStyle(
        fontFamily = AppFont,
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 14.sp
    )
)