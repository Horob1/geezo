package com.horob1.geezo.core.domain.repository

import com.horob1.geezo.core.domain.model.NetworkLog
import kotlinx.coroutines.flow.Flow

interface NetworkLogRepository {
    suspend fun insertLog(log: NetworkLog): Result<Long>
    suspend fun updateLog(log: NetworkLog): Result<Unit>
    suspend fun deleteLog(log: NetworkLog): Result<Unit>
    fun getLogCount(): Flow<Int>
    suspend fun deleteAllLogs(): Result<Unit>

    suspend fun getLogDetails(logId: Long): Result<NetworkLog>
    fun getLogDetailsFlow(logId: Long): Flow<NetworkLog?>

    suspend fun getLogs(
        limit: Int,
        offset: Int,
        isMock: Boolean,
        method: String?
    ): Result<List<NetworkLog>>

    fun getSuccessLogCount(): Flow<Int>
}
