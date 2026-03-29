package com.horob1.geezo.core.data.local

import com.horob1.geezo.core.data.local.database.dao.NetworkLogDao
import com.horob1.geezo.core.data.local.database.entity.NetworkLogEntity
import kotlinx.coroutines.flow.Flow

class NetworkLogLocalDataSource(
    private val networkLogDao: NetworkLogDao
) {
    suspend fun insertLog(log: NetworkLogEntity): Result<Long> {
        try {
            val id = networkLogDao.insertLog(log)
            return Result.success(id)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateLog(log: NetworkLogEntity): Result<Unit> {
        try {
            networkLogDao.updateLog(log)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun deleteLog(log: NetworkLogEntity): Result<Unit> {
        try {
            networkLogDao.deleteLog(log)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getLogCount(): Flow<Int> {
        return networkLogDao.getLogCount()
    }

    suspend fun deleteAllLogs(): Result<Unit> {
        try {
            networkLogDao.deleteAllLogs()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getLogDetails(logId: Long): Result<NetworkLogEntity> {
        try {
            val log = networkLogDao.getLogDetails(logId)
            return Result.success(log)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getLogDetailsFlow(logId: Long): Flow<NetworkLogEntity?> {
        return networkLogDao.getLogDetailsFlow(logId)
    }

    suspend fun getLogs(
        limit: Int,
        offset: Int,
        method: String?
    ): Result<List<NetworkLogEntity>> {
        try {
            val logs = networkLogDao.getLogs(limit, offset, method)
            return Result.success(logs)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getSuccessLogCount(): Flow<Int> {
        return networkLogDao.getSuccessLogCount()
    }
}