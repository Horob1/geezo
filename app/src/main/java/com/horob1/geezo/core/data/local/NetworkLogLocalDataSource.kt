package com.horob1.geezo.core.data.local

import com.horob1.geezo.core.data.local.database.dao.NetworkLogDao
import com.horob1.geezo.core.data.local.database.entity.NetworkLogEntity

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
}