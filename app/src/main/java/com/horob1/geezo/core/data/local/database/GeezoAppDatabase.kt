package com.horob1.geezo.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.horob1.geezo.core.data.local.database.dao.NetworkLogDao
import com.horob1.geezo.core.data.local.database.entity.NetworkLogEntity

@Database(
    entities = [
        NetworkLogEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class GeezoAppDatabase : RoomDatabase() {

    abstract fun networkLogDao(): NetworkLogDao
}
