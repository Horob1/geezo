package com.horob1.geezo.core.data.remote.api

import android.util.Log
import com.horob1.geezo.core.domain.model.NetworkLog
import com.horob1.geezo.core.domain.repository.NetworkLogRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

class KtorClientProvider(
    private val networkLogRepository: NetworkLogRepository,
    private val environment: String? = null,
    private val tag: String? = null,
    private val isMock: Boolean = false
) {

    val client: HttpClient = HttpClient(OkHttp) {
        defaultRequest {
            // Base URL and default headers if needed
        }

        engine {
            addInterceptor(createLoggingInterceptor())
        }
    }

    private fun createLoggingInterceptor() = Interceptor { chain ->
        val request = chain.request()
        val startTime = System.currentTimeMillis()

        val url = request.url.toString()
        val method = request.method
        val requestHeaders = request.headers.toString()
        val requestBodyText = readRequestBody(request)
        val requestSize = requestBodyText?.toByteArray(Charsets.UTF_8)?.size?.toLong()
        val curlCommand = buildCurlCommand(
            method = method,
            url = url,
            headers = request.headers.toMultimap(),
            body = requestBodyText
        )

        val pendingLog = NetworkLog(
            id = 0L,
            url = url,
            method = method,
            requestHeaders = requestHeaders,
            requestBody = requestBodyText,
            responseCode = null,
            responseHeaders = null,
            responseBody = null,
            errorMessage = null,
            isSuccess = false,
            startTime = startTime,
            endTime = null,
            durationMs = null,
            requestSize = requestSize,
            responseSize = null,
            environment = environment,
            tag = tag,
            curl = curlCommand,
            isMock = isMock,
            createdAt = startTime
        )

        val logId = insertPendingLog(pendingLog)

        try {
            val response = chain.proceed(request)
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime

            val responseCode = response.code
            val responseHeaders = response.headers.toString()
            val responseBodyText = readResponseBody(response)
            val responseSize = response.body.contentLength().takeIf { it >= 0 }
                ?: responseBodyText?.toByteArray(Charsets.UTF_8)?.size?.toLong()
            val success = response.isSuccessful

            updateLog(
                source = pendingLog,
                id = logId,
                responseCode = responseCode,
                responseHeaders = responseHeaders,
                responseBody = responseBodyText,
                errorMessage = if (success) null else "HTTP $responseCode",
                isSuccess = success,
                endTime = endTime,
                durationMs = duration,
                responseSize = responseSize
            )

            response
        } catch (e: Exception) {
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime

            updateLog(
                source = pendingLog,
                id = logId,
                responseCode = null,
                responseHeaders = null,
                responseBody = null,
                errorMessage = e.message ?: "Unknown network error",
                isSuccess = false,
                endTime = endTime,
                durationMs = duration,
                responseSize = null
            )
            throw e
        }
    }

    private fun readRequestBody(request: okhttp3.Request): String? = try {
        val buffer = Buffer()
        request.body?.writeTo(buffer)
        buffer.readUtf8().ifBlank { null }
    } catch (_: Exception) {
        "Error reading request payload"
    }

    private fun readResponseBody(response: Response): String? = try {
        response.peekBody(MAX_LOG_BODY_BYTES).string().ifBlank { null }
    } catch (_: Exception) {
        "Error reading response body"
    }

    private fun insertPendingLog(log: NetworkLog): Long {
        return try {
            runBlocking(Dispatchers.IO) {
                networkLogRepository.insertLog(log).getOrElse { throw it }
            }
        } catch (e: Exception) {
            Log.e("NetworkInspector", "Error inserting pending log", e)
            0L
        }
    }

    private fun updateLog(
        source: NetworkLog,
        id: Long,
        responseCode: Int?,
        responseHeaders: String?,
        responseBody: String?,
        errorMessage: String?,
        isSuccess: Boolean,
        endTime: Long,
        durationMs: Long,
        responseSize: Long?
    ) {
        if (id <= 0L) return

        val updated = source.copy(
            id = id,
            responseCode = responseCode,
            responseHeaders = responseHeaders,
            responseBody = responseBody,
            errorMessage = errorMessage,
            isSuccess = isSuccess,
            endTime = endTime,
            durationMs = durationMs,
            responseSize = responseSize
        )

        try {
            runBlocking(Dispatchers.IO) {
                networkLogRepository.updateLog(updated).getOrElse { throw it }
            }
        } catch (e: Exception) {
            Log.e("NetworkInspector", "Error updating log", e)
        }
    }

    private fun buildCurlCommand(
        method: String,
        url: String,
        headers: Map<String, List<String>>,
        body: String?
    ): String {
        val parts = mutableListOf<String>()
        parts += "curl"
        parts += "-X"
        parts += "\"$method\""

        headers.forEach { (name, values) ->
            values.forEach { value ->
                val escaped = value.replace("\"", "\\\"")
                parts += "-H"
                parts += "\"$name: $escaped\""
            }
        }

        if (!body.isNullOrBlank()) {
            val escapedBody = body.replace("\"", "\\\"")
            parts += "--data"
            parts += "\"$escapedBody\""
        }

        parts += "\"$url\""
        return parts.joinToString(" ")
    }

    companion object {
        private const val MAX_LOG_BODY_BYTES = 1024 * 1024L
    }
}
