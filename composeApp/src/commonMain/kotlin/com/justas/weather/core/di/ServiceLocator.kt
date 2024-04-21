package com.justas.weather.core.di

import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.justas.weather.core.data.network.LTMApi
import com.justas.weather.core.data.network.NOMApi
import com.justas.weather.core.data.network.OWMApi
import com.justas.weather.core.domain.repository.WeatherRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object ServiceLocator {
    val log: KermitLogger by lazy { createLogger() }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepository(
            api =
                persistentListOf(
                    nomApi,
                    owmApi,
                    ltmApi,
                ),
        )
    }

    private val nomApi: NOMApi
        get() = NOMApi(httpClient)
    private val owmApi: OWMApi
        get() = OWMApi(httpClient)
    private val ltmApi: LTMApi
        get() = LTMApi(httpClient)

    private val httpClient: HttpClient by lazy {
        createHttpClient()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun createHttpClient(): HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        useAlternativeNames = false
                        explicitNulls = false
                    },
                )
            }
            install(Logging) {
                logger =
                    object : KtorLogger {
                        override fun log(message: String) {
                            log.i(tag = "HTTP") { message.replace("\n", " ") }
                        }
                    }
                level = LogLevel.BODY
            }
        }

    private fun createLogger() =
        KermitLogger(
            config = loggerConfigInit(platformLogWriter()),
            tag = "App",
        )
}
