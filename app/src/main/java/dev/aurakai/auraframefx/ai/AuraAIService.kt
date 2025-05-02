package dev.aurakai.auraframefx.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File

class AuraAIService(
    private val backendUrl: String, // e.g., https://YOUR_CLOUD_RUN_URL
    private val idToken: String, // Google ID token from sign-in
) {
    private val client = OkHttpClient()
    private fun authHeader() = "Bearer $idToken"

    suspend fun generateText(prompt: String): String? = withContext(Dispatchers.IO) {
        val mediaType = "application/json".toMediaTypeOrNull()
        val body = """{"prompt": "$prompt"}""".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("$backendUrl/aura-gemini")
            .addHeader("Authorization", authHeader())
            .post(body)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Gemini failed: $response")
            return@withContext response.body?.string()
        }
    }

    suspend fun saveMemory(key: String, value: JSONObject): Boolean = withContext(Dispatchers.IO) {
        val mediaType = "application/json".toMediaTypeOrNull()
        val body = """{"key": "$key", "value": $value}""".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("$backendUrl/memory/save")
            .addHeader("Authorization", authHeader())
            .post(body)
            .build()
        client.newCall(request).execute().use { response ->
            return@withContext response.isSuccessful
        }
    }

    suspend fun getMemory(key: String): String? = withContext(Dispatchers.IO) {
        val url = "$backendUrl/memory/get?key=$key"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", authHeader())
            .get()
            .build()
        client.newCall(request).execute().use { response ->
            return@withContext response.body?.string()
        }
    }

    suspend fun uploadFile(file: File): Boolean = withContext(Dispatchers.IO) {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, file.asRequestBody())
            .build()
        val request = Request.Builder()
            .url("$backendUrl/upload")
            .addHeader("Authorization", authHeader())
            .post(requestBody)
            .build()
        client.newCall(request).execute().use { response ->
            return@withContext response.isSuccessful
        }
    }

    suspend fun downloadFile(filename: String): ResponseBody? = withContext(Dispatchers.IO) {
        val url = "$backendUrl/download?filename=$filename"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", authHeader())
            .get()
            .build()
        client.newCall(request).execute().use { response ->
            return@withContext response.body
        }
    }

    suspend fun analyticsQuery(query: String): String? = withContext(Dispatchers.IO) {
        val mediaType = "application/json".toMediaTypeOrNull()
        val body = """{"query": "$query"}""".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("$backendUrl/analytics/query")
            .addHeader("Authorization", authHeader())
            .post(body)
            .build()
        client.newCall(request).execute().use { response ->
            return@withContext response.body?.string()
        }
    }

    suspend fun publishPubSub(topic: String, message: String): Boolean =
        withContext(Dispatchers.IO) {
            val mediaType = "application/json".toMediaTypeOrNull()
            val body = """{"topic": "$topic", "message": "$message"}""".toRequestBody(mediaType)
            val request = Request.Builder()
                .url("$backendUrl/pubsub/publish")
                .addHeader("Authorization", authHeader())
                .post(body)
                .build()
            client.newCall(request).execute().use { response ->
                return@withContext response.isSuccessful
            }
        }

    suspend fun customizeAgent(config: JSONObject): Boolean = withContext(Dispatchers.IO) {
        val mediaType = "application/json".toMediaTypeOrNull()
        val body = config.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("$backendUrl/agent/customize")
            .addHeader("Authorization", authHeader())
            .post(body)
            .build()
        client.newCall(request).execute().use { response ->
            return@withContext response.isSuccessful
        }
    }
}