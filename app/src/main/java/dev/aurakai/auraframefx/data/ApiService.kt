package dev.aurakai.auraframefx.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService(private val context: Context) {
    private val securePrefs = SecurePreferences(context)

    init {
        try {
            SecurePreferences.initialize(context)
            securePrefs.saveApiToken("4c633ac0de5680741c7f72a392249285063f6e6c")
            recreateClient()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to initialize API service", e)
        }
    }

    fun setOAuthToken(token: String) {
        securePrefs.saveOAuthToken(token)
        recreateClient()
    }

    fun setApiToken(token: String) {
        securePrefs.saveApiToken(token)
        recreateClient()
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                val token = securePrefs.getToken()
                if (token != null) {
                    request.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(request.build())
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://your-api-endpoint.com/") // Replace with your actual API endpoint
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private var retrofit: Retrofit? = null

    private fun recreateClient() {
        retrofit = createRetrofit()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit?.create(serviceClass)
            ?: throw IllegalStateException("Retrofit client not initialized")
    }
}
