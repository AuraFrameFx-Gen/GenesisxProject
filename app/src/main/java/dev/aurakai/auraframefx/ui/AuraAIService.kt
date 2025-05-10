package dev.aurakai.auraframefx.ui.theme

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AuraAIService(
    private val accessToken: String, // Provide your OAuth2 token
    private val projectId: String,
    private val location: String = "us-central1",
    private val model: String = "gemini-pro",
) {
    private val client = OkHttpClient()
    private val endpoint =
        "https://$location-aiplatform.googleapis.com/v1/projects/$projectId/locations/$location/publishers/google/models/$model:predict"

    fun generateText(prompt: String): String? {
        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = """
        {
          \"instances\": [{\"content\": \"$prompt\"}]
        }
        """.trimIndent()
            .toRequestBody(mediaType)
        val request = Request.Builder()
            .url(endpoint)
            .addHeader("Authorization", "Bearer $accessToken")
            .post(requestBody)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Unexpected code $response")
            }
            return response.body?.string()
        }
    }
}