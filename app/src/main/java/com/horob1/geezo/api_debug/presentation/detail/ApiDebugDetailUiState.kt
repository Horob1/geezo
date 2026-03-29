package com.horob1.geezo.api_debug.presentation.detail

import com.horob1.geezo.core.domain.model.NetworkLog

data class ApiDebugDetailUiState(
    val isLoading: Boolean = false,
    val log: NetworkLog? = null,
    val errorMessage: String? = null
)

