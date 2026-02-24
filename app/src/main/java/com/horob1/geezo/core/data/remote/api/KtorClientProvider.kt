package com.horob1.geezo.core.data.remote.api

import android.util.Log
import com.horob1.geezo.core.data.local.database.dao.NetworkLogDao
import com.horob1.geezo.core.data.local.database.entity.NetworkLogEntity
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

class KtorClientProvider(
    // repo
    private val networkLogDao: NetworkLogDao
) {

    private val ioScope = CoroutineScope(Dispatchers.IO)

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

        val url = request.url.toString()
        val method = request.method
        val requestHeaders = request.headers.toString()

        val requestBodyText = try {
            request.body?.let {
                val buffer = Buffer()
                it.writeTo(buffer)
                buffer.readUtf8()
            } ?: ""
        } catch (e: Exception) {
            "Error reading request payload"
        }

        val response: Response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            saveLogToRoom(
                url = url,
                method = method,
                reqHeaders = requestHeaders,
                reqBody = requestBodyText,
                resCode = 0,
                resHeaders = "",
                resBody = e.message ?: "Unknown Network Error"
            )
            throw e
        }

        val responseCode = response.code
        val responseHeaders = response.headers.toString()

        val responseBodyText = try {
            response.peekBody(1024 * 1024L).string()
        } catch (e: Exception) {
            "Error reading response body"
        }

        saveLogToRoom(
            url = url,
            method = method,
            reqHeaders = requestHeaders,
            reqBody = requestBodyText,
            resCode = responseCode,
            resHeaders = responseHeaders,
            resBody = responseBodyText
        )

        response
    }

    private fun saveLogToRoom(
        url: String,
        method: String,
        reqHeaders: String,
        reqBody: String,
        resCode: Int,
        resHeaders: String,
        resBody: String
    ) {
        ioScope.launch {
            try {
                val log = NetworkLogEntity(
                    url = url,
                    method = method,
                    requestHeaders = reqHeaders,
                    requestBody = reqBody,
                    responseCode = resCode,
                    responseHeaders = resHeaders,
                    responseBody = resBody
                )
                networkLogDao.insertLog(log)
            } catch (e: Exception) {
                Log.e("NetworkInspector", "Error saving log to DB", e)
            }
        }
    }
}

// flow trc khi gui request => tao ra 1 log (pending)
// sau khi co response => update no lai