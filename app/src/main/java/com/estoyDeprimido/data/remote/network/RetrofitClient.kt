package com.estoyDeprimido.data.remote

import android.content.Context
import android.util.Log
import com.estoyDeprimido.data.preferences.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://cb76-212-57-66-242.ngrok-free.app"
    private const val TAG = "RetrofitClient"

    // Interceptor que añade el header "Authorization" en cada request
    private class AuthInterceptor(private val context: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            // Log de la URL que se está solicitando
            Log.d(TAG, "AuthInterceptor: Intercepting request to ${chain.request().url}")

            // Obtenemos el token de forma síncrona
            val token = runBlocking {
                val retrievedToken = UserPreferences.getToken(context)
                Log.d(TAG, "AuthInterceptor: Retrieved token: ${retrievedToken ?: "null o vacío"}")
                retrievedToken
            } ?: ""

            if (token.isBlank()) {
                Log.d(TAG, "AuthInterceptor: Token es vacío o no se encontró")
            }

            // Se añade el header Authorization con el token
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            Log.d(TAG, "AuthInterceptor: Header añadido: Authorization: Bearer $token")
            return chain.proceed(newRequest)
        }
    }

    // Función para crear el ApiService pasando el context
    fun createApiService(context: Context): ApiService {
        val authInterceptor = AuthInterceptor(context)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
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