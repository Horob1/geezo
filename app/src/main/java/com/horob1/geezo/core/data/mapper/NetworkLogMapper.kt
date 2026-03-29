package com.horob1.geezo.core.data.mapper

import com.horob1.geezo.core.data.local.database.entity.NetworkLogEntity
import com.horob1.geezo.core.domain.model.NetworkLog

fun NetworkLogEntity.toDomain(): NetworkLog {
    return NetworkLog(
        id = id,
        url = url,
        method = method,
        requestHeaders = requestHeaders,
        requestBody = requestBody,
        responseCode = responseCode,
        responseHeaders = responseHeaders,
        responseBody = responseBody,
        errorMessage = errorMessage,
        isSuccess = isSuccess,
        startTime = startTime,
        endTime = endTime,
        durationMs = durationMs,
        requestSize = requestSize,
        responseSize = responseSize,
        environment = environment,
        tag = tag,
        curl = curl,
        isMock = isMock,
        createdAt = createdAt
    )
}

fun NetworkLog.toEntity(): NetworkLogEntity {
    return NetworkLogEntity(
        id = id,
        url = url,
        method = method,
        requestHeaders = requestHeaders,
        requestBody = requestBody,
        responseCode = responseCode,
        responseHeaders = responseHeaders,
        responseBody = responseBody,
        errorMessage = errorMessage,
        isSuccess = isSuccess,
        startTime = startTime,
        endTime = endTime,
        durationMs = durationMs,
        requestSize = requestSize,
        responseSize = responseSize,
        environment = environment,
        tag = tag,
        curl = curl,
        isMock = isMock,
        createdAt = createdAt
    )
}
