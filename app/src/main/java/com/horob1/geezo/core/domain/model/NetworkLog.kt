package com.horob1.geezo.core.domain.model

data class NetworkLog(
    val id: Long,
    val url: String,
    val method: String,
    val requestHeaders: String?,
    val requestBody: String?,
    val responseCode: Int?,
    val responseHeaders: String?,
    val responseBody: String?,
    val errorMessage: String?,
    val isSuccess: Boolean,
    val startTime: Long,
    val endTime: Long?,
    val durationMs: Long?,
    val requestSize: Long?,
    val responseSize: Long?,
    val environment: String?,
    val tag: String?,
    val curl: String?,
    val isMock: Boolean,
    val createdAt: Long
)
