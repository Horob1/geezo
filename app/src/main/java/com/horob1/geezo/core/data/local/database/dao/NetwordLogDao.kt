package com.horob1.geezo.core.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.horob1.geezo.core.data.local.database.entity.NetworkLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkLogDao {

    @Insert
    suspend fun insertLog(log: NetworkLogEntity)

    @Query("SELECT * FROM network_logs ORDER BY created_at DESC")
    fun getAllLogs(): Flow<List<NetworkLogEntity>>
}
