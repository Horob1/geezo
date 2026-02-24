package com.horob1.geezo.core.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.horob1.geezo.core.data.local.database.entity.NetworkLogEntity

@Dao
interface NetworkLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: NetworkLogEntity): Long

    // Paging
}
