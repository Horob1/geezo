package com.horob1.geezo.api_debug.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horob1.geezo.core.domain.model.NetworkLog
import com.horob1.geezo.core.domain.repository.NetworkLogRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class ApiDebugDetailViewModel(
    private val networkLogRepository: NetworkLogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiDebugDetailUiState())
    val uiState = _uiState.asStateFlow()

    private var observeLogJob: Job? = null
    private var observedLogId: Long? = null

    fun loadLog(logId: Long) {
        if (observedLogId == logId && observeLogJob?.isActive == true) return

        observedLogId = logId
        observeLogJob?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        observeLogJob = if (USE_MOCK_DATA) {
            viewModelScope.launch {
                // Simulate loading time so UI behavior matches real data mode.
                delay(220)
                val mockLog = buildMockLog(logId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        log = mockLog,
                        errorMessage = null
                    )
                }
            }
        } else {
            viewModelScope.launch {
                networkLogRepository.getLogDetailsFlow(logId).collect { log ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            log = log,
                            errorMessage = if (log == null) "Log not found" else null
                        )
                    }
                }
            }
        }
    }

    private fun buildMockLog(logId: Long): NetworkLog {
        val methods = listOf("GET", "POST", "PUT", "PATCH", "DELETE")
        val method = methods.random()
        val startTime = System.currentTimeMillis() - Random.nextLong(2_000L, 20_000L)
        val duration = Random.nextLong(40L, 1_800L)
        val endTime = startTime + duration
        val isSuccess = Random.nextInt(100) < 80
        val responseCode = if (isSuccess) listOf(200, 201, 204).random() else listOf(
            400,
            401,
            403,
            404,
            429,
            500
        ).random()

        val requestHeaders =
            "Authorization: Bearer debug-mock-token\nContent-Type: application/json\nX-Mock-Trace: $logId"
        val requestBody =
            if (method == "GET" || method == "DELETE") null else "{\"id\":$logId,\"name\":\"mock-item-$logId\"}"
        val responseHeaders = "Content-Type: application/json\nX-Response-Time: ${duration}ms"
        val responseBody = if (isSuccess) {
            "{\"ok\":true,\"id\":$logId,\"message\":\"Mock detail data\"}"
        } else {
            "{\"ok\":false,\"error\":\"Mock failure\",\"id\":$logId}"
        }

        return NetworkLog(
            id = logId,
            url = "https://api.geezo.dev/v1/items/$logId",
            method = method,
            requestHeaders = requestHeaders,
            requestBody = requestBody,
            responseCode = responseCode,
            responseHeaders = responseHeaders,
            responseBody = responseBody,
            errorMessage = if (isSuccess) null else "HTTP $responseCode",
            isSuccess = isSuccess,
            startTime = startTime,
            endTime = endTime,
            durationMs = duration,
            requestSize = requestBody?.toByteArray()?.size?.toLong() ?: 0L,
            responseSize = responseBody.toByteArray().size.toLong(),
            environment = "mock",
            tag = "api_debug_detail",
            curl = "curl -X \"$method\" \"https://api.geezo.dev/v1/items/$logId\"",
            isMock = true,
            createdAt = startTime
        )
    }

    private companion object {
        const val USE_MOCK_DATA = false
    }
}
