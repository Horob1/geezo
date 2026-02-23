package com.horob1.geezo.core.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "network_logs")
data class NetworkLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "method")
    val method: String,

    @ColumnInfo(name = "request_headers")
    val requestHeaders: String,

    @ColumnInfo(name = "request_body")
    val requestBody: String?,

    @ColumnInfo(name = "response_code")
    val responseCode: Int?,

    @ColumnInfo(name = "response_headers")
    val responseHeaders: String?,

    @ColumnInfo(name = "response_body")
    val responseBody: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)