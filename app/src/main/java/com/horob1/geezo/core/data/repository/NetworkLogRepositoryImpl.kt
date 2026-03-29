package com.horob1.geezo.core.data.repository

import com.horob1.geezo.core.data.local.NetworkLogLocalDataSource
import com.horob1.geezo.core.data.mapper.toDomain
import com.horob1.geezo.core.data.mapper.toEntity
import com.horob1.geezo.core.domain.model.NetworkLog
import com.horob1.geezo.core.domain.repository.NetworkLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class NetworkLogRepositoryImpl(
    private val localDataSource: NetworkLogLocalDataSource
) : NetworkLogRepository {

    override suspend fun insertLog(log: NetworkLog): Result<Long> {
        return localDataSource.insertLog(log.toEntity())
    }

    override suspend fun updateLog(log: NetworkLog): Result<Unit> {
        return localDataSource.updateLog(log.toEntity())
    }

    override suspend fun deleteLog(log: NetworkLog): Result<Unit> {
        return localDataSource.deleteLog(log.toEntity())
    }

    override fun getLogCount(): Flow<Int> {
        return localDataSource.getLogCount()
    }

    override suspend fun deleteAllLogs(): Result<Unit> {
        return localDataSource.deleteAllLogs()
    }

    override suspend fun getLogDetails(logId: Long): Result<NetworkLog> {
        return localDataSource.getLogDetails(logId).map { it.toDomain() }
    }

    override fun getLogDetailsFlow(logId: Long): Flow<NetworkLog?> {
        return localDataSource.getLogDetailsFlow(logId).map { it?.toDomain() }
    }

    override suspend fun getLogs(
        limit: Int,
        offset: Int,
        isMock: Boolean,
        method: String?
    ): Result<List<NetworkLog>> {
        return if (isMock) {
            Result.success(mockLogs(limit, method))
        } else {
            localDataSource.getLogs(limit, offset, method)
                .map { logs -> logs.map { it.toDomain() } }
        }
    }

    override fun getSuccessLogCount(): Flow<Int> {
        return localDataSource.getSuccessLogCount()
    }


    fun mockLogs(length: Int, method: String?): List<NetworkLog> {
        val safeLength = length.coerceIn(1, 500)
        val methods = listOf("GET", "POST", "PUT", "PATCH", "DELETE")

        return List(safeLength) { index ->
            val selectedMethod = method?.uppercase() ?: methods.random()
            val startTime = System.currentTimeMillis() - Random.nextLong(5_000L, 120_000L)
            val duration = Random.nextLong(20L, 2_500L)
            val endTime = startTime + duration
            val success = Random.nextInt(100) < 80
            val code = if (success) listOf(200, 201, 202, 204).random() else listOf(
                400,
                401,
                403,
                404,
                429,
                500
            ).random()
            val reqBody =
                if (selectedMethod == "GET" || selectedMethod == "DELETE") null else "{\"name\":\"mock-$index\"}"
            val resBody =
                if (success) "{\"ok\":true,\"id\":$index}" else "{\"ok\":false,\"error\":\"mock failure\"}"

            NetworkLog(
                id = (index + 1).toLong(),
                url = "https://api.geezo.dev/v1/resource/${index + 1}",
                method = selectedMethod,
                requestHeaders = "Authorization: Bearer mock-token\nContent-Type: application/json",
                requestBody = reqBody,
                responseCode = code,
                responseHeaders = "Content-Type: application/json",
                responseBody = resBody,
                errorMessage = if (success) null else "HTTP $code",
                isSuccess = success,
                startTime = startTime,
                endTime = endTime,
                durationMs = duration,
                requestSize = reqBody?.toByteArray()?.size?.toLong() ?: 0L,
                responseSize = resBody.toByteArray().size.toLong(),
                environment = "dev",
                tag = "mock",
                curl = "curl -X \"$selectedMethod\" \"https://api.geezo.dev/v1/resource/${index + 1}\"",
                isMock = true,
                createdAt = startTime
            )
        }
    }
}
