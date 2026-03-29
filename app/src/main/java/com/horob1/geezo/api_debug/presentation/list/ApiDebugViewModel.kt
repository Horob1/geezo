package com.horob1.geezo.api_debug.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horob1.geezo.core.domain.repository.NetworkLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ApiDebugViewModel(
    private val networkLogRepository: NetworkLogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiDebugUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeCounts()
        loadFirstPage()
    }

    fun onMethodChanged(method: String?) {
        _uiState.update { it.copy(selectedMethod = method) }
        loadFirstPage()
    }

    fun onRefresh() {
        loadFirstPage()
    }

    fun onLoadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasNextPage) return

        val nextPage = state.currentPage + 1
        val offset = nextPage * PAGE_SIZE

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, errorMessage = null) }

            networkLogRepository.getLogs(
                limit = PAGE_SIZE,
                offset = offset,
                isMock = USE_MOCK_DATA,
                method = _uiState.value.selectedMethod
            ).onSuccess { nextLogs ->
                _uiState.update { latest ->
                    latest.copy(
                        currentPage = nextPage,
                        logs = latest.logs + nextLogs,
                        isLoadingMore = false,
                        hasNextPage = nextLogs.size == PAGE_SIZE
                    )
                }
            }.onFailure { exception ->
                _uiState.update { latest ->
                    latest.copy(
                        isLoadingMore = false,
                        errorMessage = exception.message ?: "Load more logs failed"
                    )
                }
            }
        }
    }

    fun onDeleteAllLogs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            networkLogRepository.deleteAllLogs()
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            currentPage = 0,
                            logs = emptyList(),
                            isLoading = false,
                            isLoadingMore = false,
                            hasNextPage = false
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Delete logs failed"
                        )
                    }
                }
        }
    }

    private fun loadFirstPage() {
        if (_uiState.value.isLoading || _uiState.value.isLoadingMore) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currentPage = 0,
                    isLoading = true,
                    isLoadingMore = false,
                    errorMessage = null,
                    hasNextPage = true
                )
            }

            networkLogRepository.getLogs(
                limit = PAGE_SIZE,
                offset = 0,
                isMock = USE_MOCK_DATA,
                method = _uiState.value.selectedMethod
            ).onSuccess { logs ->
                _uiState.update { state ->
                    state.copy(
                        currentPage = 0,
                        logs = logs,
                        isLoading = false,
                        hasNextPage = logs.size == PAGE_SIZE
                    )
                }
            }.onFailure { exception ->
                _uiState.update { state ->
                    state.copy(
                        currentPage = 0,
                        logs = emptyList(),
                        isLoading = false,
                        hasNextPage = false,
                        errorMessage = exception.message ?: "Load logs failed"
                    )
                }
            }
        }
    }

    private fun observeCounts() {
        viewModelScope.launch {
            networkLogRepository.getLogCount().collect { totalCount ->
                _uiState.update { state -> state.copy(totalLogsCount = totalCount) }
            }
        }

        viewModelScope.launch {
            networkLogRepository.getSuccessLogCount().collect { successCount ->
                _uiState.update { state -> state.copy(successLogsCount = successCount) }
            }
        }
    }

    private companion object {
        const val USE_MOCK_DATA = false
        const val PAGE_SIZE = 20
    }
}