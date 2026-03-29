package com.horob1.geezo.api_debug.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.horob1.geezo.R
import com.horob1.geezo.core.domain.model.NetworkLog
import com.horob1.geezo.core.presentation.theme.GeezoColor
import com.horob1.geezo.core.presentation.theme.dimens
import org.koin.androidx.compose.koinViewModel

@Composable
fun ApiDebugDetailRoute(
    logId: Long,
    onBack: () -> Unit,
    viewModel: ApiDebugDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(logId) {
        viewModel.loadLog(logId)
    }

    ApiDebugDetailScreen(
        uiState = uiState,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiDebugDetailScreen(
    uiState: ApiDebugDetailUiState,
    onBack: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var selectedTab by remember { mutableIntStateOf(0) }

    val detailTabs = listOf(
        stringResource(id = R.string.api_debug_detail_tab_headers),
        stringResource(id = R.string.api_debug_detail_tab_payload),
        stringResource(id = R.string.api_debug_detail_tab_response)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = GeezoColor.Primary,
                    navigationIconContentColor = GeezoColor.Primary
                ),
                title = { Text(text = stringResource(id = R.string.api_debug_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.common_back)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(MaterialTheme.dimens.s3),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.s2)
            ) {
                Button(
                    onClick = {
                        val curl = uiState.log?.curl.orEmpty()
                        clipboardManager.setText(AnnotatedString(curl))
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.log?.curl.isNullOrBlank()
                ) {
                    Text(text = stringResource(id = R.string.api_debug_detail_copy_curl))
                }

                Button(
                    onClick = {
                        val response = uiState.log?.responseBody.orEmpty()
                        clipboardManager.setText(AnnotatedString(response))
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.log?.responseBody.isNullOrBlank()
                ) {
                    Text(text = stringResource(id = R.string.api_debug_detail_copy_response))
                }
            }
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = GeezoColor.Primary)
                }
            }

            uiState.log == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(MaterialTheme.dimens.s3),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = uiState.errorMessage ?: stringResource(id = R.string.api_debug_detail_not_found))
                }
            }

            else -> {
                val log = uiState.log
                val tabContent = when (selectedTab) {
                    0 -> buildHeaderContent(
                        log = log,
                        requestTitle = stringResource(id = R.string.api_debug_detail_request_headers),
                        responseTitle = stringResource(id = R.string.api_debug_detail_response_headers),
                        emptyRequest = stringResource(id = R.string.api_debug_detail_no_request_headers),
                        emptyResponse = stringResource(id = R.string.api_debug_detail_no_response_headers)
                    )

                    1 -> buildPayloadContent(
                        log = log,
                        emptyPayload = stringResource(id = R.string.api_debug_detail_no_payload)
                    )

                    else -> buildResponseContent(
                        log = log,
                        emptyResponseBody = stringResource(id = R.string.api_debug_detail_no_response_body)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(
                                horizontal = MaterialTheme.dimens.s3,
                                vertical = MaterialTheme.dimens.s2
                            )
                    ) {
                        GeneralInfoSection(log = log)

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.s3))

                        TabRow(selectedTabIndex = selectedTab) {
                            detailTabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = { Text(text = title) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.s2))

                        SelectionContainer {
                            Text(text = tabContent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GeneralInfoSection(log: NetworkLog) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.s1)) {
        Text(text = stringResource(id = R.string.api_debug_detail_general), style = MaterialTheme.typography.titleMedium)
        InfoRow(label = stringResource(id = R.string.api_debug_detail_label_method), value = log.method)
        InfoRow(label = stringResource(id = R.string.api_debug_detail_label_url), value = log.url)
        InfoRow(
            label = stringResource(id = R.string.api_debug_detail_label_status),
            value = if (log.isSuccess) {
                stringResource(id = R.string.api_debug_status_success)
            } else {
                stringResource(id = R.string.api_debug_status_error)
            }
        )
        InfoRow(label = stringResource(id = R.string.api_debug_detail_label_code), value = log.responseCode?.toString() ?: "-")
        InfoRow(
            label = stringResource(id = R.string.api_debug_detail_label_duration),
            value = log.durationMs?.let { stringResource(id = R.string.api_debug_duration_ms, it) } ?: "-"
        )
        InfoRow(label = stringResource(id = R.string.api_debug_detail_label_request_size), value = log.requestSize?.toString() ?: "-")
        InfoRow(label = stringResource(id = R.string.api_debug_detail_label_response_size), value = log.responseSize?.toString() ?: "-")
        InfoRow(label = stringResource(id = R.string.api_debug_detail_label_environment), value = log.environment ?: "-")
        InfoRow(label = stringResource(id = R.string.api_debug_detail_label_tag), value = log.tag ?: "-")
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun buildHeaderContent(
    log: NetworkLog,
    requestTitle: String,
    responseTitle: String,
    emptyRequest: String,
    emptyResponse: String
): String {
    val request = log.requestHeaders.orEmpty().ifBlank { emptyRequest }
    val response = log.responseHeaders.orEmpty().ifBlank { emptyResponse }
    return "$requestTitle\n$request\n\n$responseTitle\n$response"
}

private fun buildPayloadContent(
    log: NetworkLog,
    emptyPayload: String
): String {
    return log.requestBody.orEmpty().ifBlank { emptyPayload }
}

private fun buildResponseContent(
    log: NetworkLog,
    emptyResponseBody: String
): String {
    return log.responseBody.orEmpty().ifBlank {
        log.errorMessage ?: emptyResponseBody
    }
}
