package com.horob1.geezo.api_debug.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.horob1.geezo.R
import com.horob1.geezo.core.domain.model.NetworkLog
import com.horob1.geezo.core.presentation.theme.GeezoColor
import com.horob1.geezo.core.presentation.theme.dimens
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val methods = listOf("GET", "POST", "PUT", "PATCH", "DELETE")

@Composable
fun ApiDebugRoute(
    onBack: () -> Unit,
    onOpenDetail: (Long) -> Unit,
    viewModel: ApiDebugViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ApiDebugScreen(
        selectedMethod = uiState.selectedMethod,
        totalLogsCount = uiState.totalLogsCount,
        successLogsCount = uiState.successLogsCount,
        logs = uiState.logs,
        isLoading = uiState.isLoading,
        isLoadingMore = uiState.isLoadingMore,
        hasNextPage = uiState.hasNextPage,
        errorMessage = uiState.errorMessage,
        onMethodClick = viewModel::onMethodChanged,
        onRefresh = viewModel::onRefresh,
        onDeleteAllLogs = viewModel::onDeleteAllLogs,
        onLoadMore = viewModel::onLoadNextPage,
        onBack = onBack,
        onOpenDetail = onOpenDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ApiDebugScreen(
    selectedMethod: String? = null,
    totalLogsCount: Int = 0,
    successLogsCount: Int = 0,
    logs: List<NetworkLog> = emptyList(),
    isLoading: Boolean = false,
    isLoadingMore: Boolean = false,
    hasNextPage: Boolean = true,
    errorMessage: String? = null,
    onMethodClick: (String?) -> Unit = {},
    onRefresh: () -> Unit = {},
    onDeleteAllLogs: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onBack: () -> Unit = {},
    onOpenDetail: (Long) -> Unit = {}
) {
    val isShowConfirmDeleteDialog = rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val shouldLoadMore by remember(logs, isLoading, isLoadingMore, hasNextPage) {
        derivedStateOf {
            if (logs.isEmpty() || isLoading || isLoadingMore || !hasNextPage) {
                false
            } else {
                val lastVisibleItemIndex =
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                val totalItems = listState.layoutInfo.totalItemsCount
                totalItems > 0 && lastVisibleItemIndex >= totalItems - 3
            }
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = GeezoColor.Primary,
                    navigationIconContentColor = GeezoColor.Primary,
                    actionIconContentColor = GeezoColor.Primary
                ),
                title = { Text(text = stringResource(id = R.string.api_debug_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = stringResource(id = R.string.common_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.api_debug_reload)
                        )
                    }
                    IconButton(onClick = { isShowConfirmDeleteDialog.value = true }) {
                        Icon(
                            Icons.Default.RestoreFromTrash,
                            contentDescription = stringResource(id = R.string.api_debug_delete_all)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                modifier = Modifier.padding(vertical = MaterialTheme.dimens.s4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.s1)
            ) {
                item { Spacer(modifier = Modifier.width(MaterialTheme.dimens.s4)) }
                item {
                    MethodButton(
                        method = null,
                        isSelected = selectedMethod == null,
                        onClick = { onMethodClick(null) }
                    )
                }
                items(methods) { method ->
                    MethodButton(
                        method = method,
                        isSelected = selectedMethod == method,
                        onClick = { onMethodClick(method) }
                    )
                }
                item { Spacer(modifier = Modifier.width(MaterialTheme.dimens.s4)) }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = GeezoColor.Primary.copy(alpha = 0.1f))
                    .padding(
                        horizontal = MaterialTheme.dimens.s3,
                        vertical = MaterialTheme.dimens.s2
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.api_debug_total_logs, totalLogsCount),
                    color = GeezoColor.Primary
                )
                Text(
                    text = stringResource(id = R.string.api_debug_success_logs, successLogsCount),
                    color = GeezoColor.Primary
                )
            }

            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage,
                    color = GeezoColor.Error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.dimens.s3)
                )
            }

            if (isLoading && logs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GeezoColor.Primary)
                }
            } else if (logs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.api_debug_no_logs))
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.dimens.s3),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.s2)
                ) {
                    items(items = logs, key = { it.id }) { log ->
                        NetworkLogItem(
                            method = log.method,
                            url = log.url,
                            status = log.toStatus(),
                            size = (log.responseSize ?: log.requestSize).toSizeLabel(),
                            duration = log.durationMs.toDurationLabel(),
                            date = log.createdAt.toDateLabel(),
                            onClick = { onOpenDetail(log.id) }
                        )
                    }

                    if (isLoadingMore) {
                        item(key = "paging_loader") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = MaterialTheme.dimens.s3),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = GeezoColor.Primary)
                            }
                        }
                    }
                }
            }
        }

        if (isShowConfirmDeleteDialog.value) {
            ConfirmDeleteDialog(
                onDismiss = { isShowConfirmDeleteDialog.value = false },
                onConfirm = {
                    isShowConfirmDeleteDialog.value = false
                    onDeleteAllLogs()
                }
            )
        }
    }
}

@Composable
fun MethodButton(
    method: String?,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val allLabel = stringResource(id = R.string.api_debug_all_methods)
    if (isSelected) {
        Button(
            onClick = {},
            modifier = Modifier.padding(MaterialTheme.dimens.s1)
        ) {
            Text(text = method ?: allLabel)
        }
    } else {
        ElevatedButton(
            onClick = onClick,
            modifier = Modifier.padding(MaterialTheme.dimens.s1)
        ) {
            Text(text = method ?: allLabel)
        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.api_debug_delete_all_title), color = GeezoColor.Primary) },
        text = { Text(text = stringResource(id = R.string.api_debug_delete_all_message)) },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.common_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.common_delete))
            }
        }
    )
}

enum class ENetworkLogStatus(
    val color: Color
) {
    Success(
        color = GeezoColor.Success
    ),
    Error(
        color = GeezoColor.Error
    )
}

@Composable
fun NetworkLogItem(
    method: String = "GET",
    url: String = "https://example.com",
    status: ENetworkLogStatus = ENetworkLogStatus.Success,
    size: String = "123 KB",
    duration: String = "10ms",
    date: String = "2023-07-01 12:00:00",
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.s3)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.s2),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                GeezoColor.Primary.copy(alpha = 0.1f),
                                MaterialTheme.shapes.small
                            )
                            .border(
                                width = 1.dp,
                                color = GeezoColor.Primary,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(MaterialTheme.dimens.s1)
                    ) {
                        Text(text = method, color = GeezoColor.Primary)
                    }

                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                status.color.copy(alpha = 0.1f),
                                MaterialTheme.shapes.small
                            )
                            .border(
                                width = 1.dp,
                                color = status.color,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(MaterialTheme.dimens.s1)
                    ) {
                        Text(
                            text = if (status == ENetworkLogStatus.Success) {
                                stringResource(id = R.string.api_debug_status_success)
                            } else {
                                stringResource(id = R.string.api_debug_status_error)
                            },
                            color = status.color
                        )
                    }
                }

                Text(text = date)

            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.s2))

            Text(
                text = url,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textDecoration = MaterialTheme.typography.bodyLarge.textDecoration
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.s2))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (size.isNotBlank()) {
                    Text(text = size)
                }
                if (duration.isNotBlank()) {
                    Text(text = duration)
                }
            }
        }
    }
}

private fun NetworkLog.toStatus(): ENetworkLogStatus {
    return if (isSuccess) ENetworkLogStatus.Success else ENetworkLogStatus.Error
}

private fun Long?.toSizeLabel(): String {
    val value = this ?: return ""
    if (value <= 0L) return ""

    val kb = value / 1024.0
    return if (kb >= 1024.0) {
        String.format(Locale.US, "%.2f MB", kb / 1024.0)
    } else {
        String.format(Locale.US, "%.2f KB", kb)
    }
}

private fun Long?.toDurationLabel(): String {
    val value = this ?: return ""
    if (value <= 0L) return ""
    return "$value ms"
}

private fun Long.toDateLabel(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(Date(this))
}

@Preview(showBackground = true)
@Composable
private fun NetworkLogItemPreview() {
    NetworkLogItem()
}
