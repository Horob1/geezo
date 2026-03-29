package com.horob1.geezo.core.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.horob1.geezo.core.data.local.database.entity.NetworkLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: NetworkLogEntity): Long

    @Update
    suspend fun updateLog(log: NetworkLogEntity)

    @Delete
    suspend fun deleteLog(log: NetworkLogEntity)

    @Query("SELECT COUNT(*) FROM network_logs")
    fun getLogCount(): Flow<Int>

    @Query("DELETE FROM network_logs")
    suspend fun deleteAllLogs()

    @Query("SELECT * FROM network_logs WHERE id = :logId")
    suspend fun getLogDetails(logId: Long): NetworkLogEntity

    @Query("SELECT * FROM network_logs WHERE id = :logId LIMIT 1")
    fun getLogDetailsFlow(logId: Long): Flow<NetworkLogEntity?>

    @Query("SELECT * FROM network_logs WHERE (:method IS NULL OR UPPER(method) = UPPER(:method)) ORDER BY createdAt DESC, id DESC LIMIT :limit OFFSET :offset")
    suspend fun getLogs(
        limit: Int,
        offset: Int,
        method: String?
    ): List<NetworkLogEntity>

    @Query("SELECT COUNT(*) FROM network_logs WHERE isSuccess = 1")
    fun getSuccessLogCount(): Flow<Int>

}
