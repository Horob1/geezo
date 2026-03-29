package com.horob1.geezo.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object OnBoarding : Routes()

    @Serializable
    data object Auth : Routes()

    @Serializable
    data object Main : Routes()

    // Top-level entry route for api_debug module.
    @Serializable
    data object ApiDebug : Routes()
}
