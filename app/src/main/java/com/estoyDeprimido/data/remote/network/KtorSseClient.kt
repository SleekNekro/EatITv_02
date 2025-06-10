package com.estoyDeprimido.data.remote.network

import android.content.Context
import com.estoyDeprimido.data.preferences.UserPreferences
import com.estoyDeprimido.data.remote.RetrofitClient.BASE_URL
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.isSuccess
import io.ktor.network.tls.CipherSuite
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager
import kotlinx.coroutines.delay
import kotlinx.io.IOException
import okhttp3.TlsVersion

object KtorSseClient {
    var cachedToken: String? = null

    suspend fun initializeToken(context: Context) {
        println("🔎 Entrando en initializeToken()...")
        cachedToken = UserPreferences.getToken(context)
        println("✅ Token recuperado y asignado en KtorSseClient: $cachedToken")
    }

    fun updateToken(token: String) {
        cachedToken = token
        println("✅ Token actualizado en KtorSseClient manualmente: $cachedToken")
    }

    private val client = HttpClient(CIO) {
        engine {
            https {
                trustManager = object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            }
        }
        install(ContentNegotiation) {
            Json { ignoreUnknownKeys = true }
        }
    }

    suspend fun listenToEvents(context: Context): Flow<Map<String, Any>> = flow {
        if (cachedToken.isNullOrEmpty()) {
            println("⏳ Intentando recuperar token JWT desde almacenamiento...")
            initializeToken(context)
        }

        while (cachedToken.isNullOrEmpty()) {
            println("⏳ Esperando que el token se actualice en KtorSseClient...")
            delay(2000)
        }

        println("✅ Token disponible, iniciando conexión SSE...")

        val endpoints = listOf(
            "$BASE_URL/recipe/events",
            "$BASE_URL/like/events"
        )

        while (true) {
            try {
                for (url in endpoints) {
                    println("🔗 Conectando a SSE en: $url")
                    val response = client.get(url) {
                        header("Authorization", "Bearer ${cachedToken.orEmpty()}")
                        header("Accept", "text/event-stream")
                    }

                    if (!response.status.isSuccess()) {
                        println("⚠️ Error en conexión SSE (${response.status}) - Reintentando en 5s...")
                        delay(5000)
                        continue
                    }

                    val channel = response.bodyAsChannel()
                    while (!channel.isClosedForRead) {
                        val line = channel.readUTF8Line()
                        println("🔍 Datos SSE recibidos: $line")

                        line?.takeIf { it.startsWith("data:") }?.let {
                            val jsonData = it.removePrefix("data: ").trim()

                            if (jsonData.isNotEmpty() && jsonData.startsWith("{") && jsonData.endsWith("}")) { // 🔥 Verificar formato JSON
                                try {
                                    val event = Json.decodeFromString<Map<String, Any>>(jsonData)

                                    val category = when (url) {
                                        endpoints[0] -> "recipe"
                                        endpoints[1] -> "like"
                                        else -> "unknown"
                                    }

                                    emit(mapOf("category" to category, "data" to event))
                                } catch (e: Exception) {
                                    println("⚠️ Error al procesar evento SSE: ${e.message}")
                                }
                            } else {
                                println("⚠️ Evento SSE ignorado: No es un JSON válido")
                            }
                        }
                    }

                    channel.cancel(IOException("Error en conexión SSE")) // 🔥 Cierra el canal antes de volver a intentar conexión
                }
            } catch (e: Exception) {
                println("⚠️ Error SSE: ${e.message} - Intentando reconectar en 5s...")
                delay(5000)
            }
        }
    }


    suspend fun listenToLikeEvents(context: Context): Flow<Map<String, Any>> = flow {
        if (cachedToken.isNullOrEmpty()) {
            println("⏳ Intentando recuperar token JWT desde almacenamiento...")
            initializeToken(context)
        }

        while (cachedToken.isNullOrEmpty()) {
            println("⏳ Esperando que el token se actualice en KtorSseClient...")
            delay(2000)
        }

        println("✅ Token disponible, escuchando eventos de likes...")

        try {
            val response = client.get("https://57d3-212-57-66-242.ngrok-free.app/like/events") {
                header("Authorization", "Bearer ${cachedToken.orEmpty()}")
                header("Accept", "text/event-stream")
            }
            val channel = response.bodyAsChannel()

            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line()
                line?.takeIf { it.startsWith("data:") }?.let {
                    val jsonData = it.removePrefix("data: ").trim()
                    val event = Json.decodeFromString<Map<String, Any>>(jsonData)

                    if (event["userId"] == 15) { // 🔥 Verifica si el evento corresponde al usuario actual
                        println("🔄 Like actualizado: Receta ${event["recipeId"]} - Estado: ${event["hasLiked"]}")
                        emit(event) // 🔥 Emitir evento para actualizar la UI
                    }
                }
            }
        } catch (e: Exception) {
            println("⚠️ Error SSE Like: ${e.message} - Intentando reconectar en 5s...")
            delay(5000)
        }
    }
}
