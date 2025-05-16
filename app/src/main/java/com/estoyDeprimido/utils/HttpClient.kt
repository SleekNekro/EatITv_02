package com.estoyDeprimido.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

val client = HttpClient(CIO){
    install(ContentNegotiation){
        Json { ignoreUnknownKeys }
    }
}