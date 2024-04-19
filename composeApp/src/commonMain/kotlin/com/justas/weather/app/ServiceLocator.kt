package com.justas.weather.app

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlin.native.concurrent.ThreadLocal
import kotlinx.serialization.json.Json

@ThreadLocal
object ServiceLocator {
    val httpClient: HttpClient by lazy {
        createHttpClient()
    }

    private fun createHttpClient(): HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        useAlternativeNames = false
                    },
                )
            }
        }
}
