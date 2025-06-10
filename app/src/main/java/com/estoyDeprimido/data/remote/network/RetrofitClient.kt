package com.estoyDeprimido.data.remote

import android.content.Context
import com.estoyDeprimido.data.preferences.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitClient {
    const val BASE_URL = "https://eatitv03-production.up.railway.app"
    private const val TAG = "RetrofitClient"

    private var cachedToken: String? = null

    fun updateToken(token: String) {
        cachedToken = token
        println("✅ Token actualizado en RetrofitClient: $cachedToken")
    }

    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = cachedToken ?: ""
            val currentHost = BASE_URL.substringAfter("//").substringBefore("/")

            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            return try {
                chain.proceed(newRequest)
            } catch (e: Exception) {
                println("⚠️ Error en AuthInterceptor: ${e.message}")
                throw e
            }
        }
    }

    fun createApiService(context: Context): ApiService {
        val token = runBlocking { UserPreferences.getToken(context) } ?: ""
        updateToken(token)

        val authInterceptor = AuthInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            }
        )

        val sslContext = SSLContext.getInstance("TLSv1.2")  // 🔥 Forzar TLS 1.2
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS)) // 🔥 Eliminado CLEARTEXT
            //.hostnameVerifier { _, _ -> true }  // 🔥 Comentado para pruebas
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
