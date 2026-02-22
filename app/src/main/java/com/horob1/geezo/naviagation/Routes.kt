package com.horob1.geezo.naviagation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object OnBoarding : Routes()


    @Serializable
    data object Auth : Routes()

    @Serializable
    data object Main : Routes()

    @Serializable
    data object ApiDebug: Routes()

}

