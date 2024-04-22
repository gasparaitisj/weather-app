package com.justas.weather.core.di

import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.justas.weather.core.data.network.FMIApi
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
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.QName
import nl.adaptivity.xmlutil.XmlReader
import nl.adaptivity.xmlutil.serialization.DefaultXmlSerializationPolicy
import nl.adaptivity.xmlutil.serialization.InputKind
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.structure.XmlDescriptor

object ServiceLocator {
    val log: KermitLogger by lazy { createLogger() }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepository(
            api =
                persistentListOf(
                    ltmApi,
                    owmApi,
                    nomApi,
                    fmiApi,
                ),
        )
    }

    private val nomApi: NOMApi by lazy {
        NOMApi(httpClient)
    }
    private val owmApi: OWMApi by lazy {
        OWMApi(httpClient)
    }
    private val ltmApi: LTMApi by lazy {
        LTMApi(httpClient)
    }

    private val fmiApi: FMIApi by lazy {
        FMIApi(httpClient, xmlUtil)
    }

    private val httpClient: HttpClient by lazy {
        createHttpClient()
    }

    private val xmlUtil: XML by lazy {
        createXmlUtil()
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

    @OptIn(ExperimentalXmlUtilApi::class)
    private fun createXmlUtil(): XML =
        XML {
            policy =
                DefaultXmlSerializationPolicy {
                    // Ignore unknown fields
                    unknownChildHandler =
                        UnknownChildHandler {
                                input: XmlReader,
                                inputKind: InputKind,
                                descriptor: XmlDescriptor,
                                name: QName?,
                                candidates: Collection<Any> ->
                            emptyList()
                        }
                }
        }

    private fun createLogger() =
        KermitLogger(
            config = loggerConfigInit(platformLogWriter()),
            tag = "App",
        )
}
