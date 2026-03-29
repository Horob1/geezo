package com.horob1.geezo.core.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_logs")
data class NetworkLogEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // 🔹 Request
    val url: String,
    val method: String,

    val requestHeaders: String?,
    val requestBody: String?,

    // 🔹 Response
    val responseCode: Int?,
    val responseHeaders: String?,
    val responseBody: String?,

    // 🔹 Error
    val errorMessage: String?,
    val isSuccess: Boolean,

    // 🔹 Time tracking
    val startTime: Long,
    val endTime: Long?,
    val durationMs: Long?,

    // 🔹 Size (debug performance)
    val requestSize: Long?,   // bytes
    val responseSize: Long?,  // bytes

    // 🔹 Extra metadata
    val environment: String?, // dev / staging / prod
    val tag: String?,         // feature/module
    val curl: String?,
    val isMock: Boolean = false,

    // 🔹 Created
    val createdAt: Long = System.currentTimeMillis()
)