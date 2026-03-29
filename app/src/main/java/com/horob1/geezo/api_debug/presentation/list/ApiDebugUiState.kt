package com.horob1.geezo.api_debug.presentation.list

import com.horob1.geezo.core.domain.model.NetworkLog

data class ApiDebugUiState(
    val selectedMethod: String? = null,
    val logs: List<NetworkLog> = emptyList(),
    val totalLogsCount: Int = 0,
    val successLogsCount: Int = 0,
    val currentPage: Int = 0,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasNextPage: Boolean = true,
    val errorMessage: String? = null
)
