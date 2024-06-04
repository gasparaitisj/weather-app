package com.justas.weather.core.domain.repository

import app.cash.turbine.test
import com.justas.weather.core.data.network.ltm.LTMForecastApi
import com.justas.weather.core.di.ServiceLocator
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.CommonPlace
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.test.runTest

class ForecastRepositoryTest {
    companion object {
        private val response =
            """
{
    "place": {
        "code": "vilnius",
        "name": "Vilnius",
        "administrativeDivision": "Vilniaus miesto savivaldybė",
        "country": "Lietuva",
        "countryCode": "LT",
        "coordinates": {
            "latitude": 54.687046,
            "longitude": 25.282911
        }
    },
    "forecastType": "long-term",
    "forecastCreationTimeUtc": "9999-01-01 01:01:00",
    "forecastTimestamps": [
        {
            "forecastTimeUtc": "9999-01-01 01:01:00",
            "airTemperature": 14.2,
            "feelsLikeTemperature": 14.2,
            "windSpeed": 3,
            "windGust": 8,
            "windDirection": 124,
            "cloudCover": 41,
            "seaLevelPressure": 1008,
            "relativeHumidity": 39,
            "totalPrecipitation": 0,
            "conditionCode": "partly-cloudy"
        }
    ]
}
            """.trimIndent()
        private val mockEngine =
            MockEngine { _ ->
                respond(
                    content = ByteReadChannel(response),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        private val httpClient = ServiceLocator.createHttpClient(mockEngine)
        private val forecastRepository =
            ForecastRepository(
                forecastApis =
                    persistentListOf(
                        LTMForecastApi(httpClient),
                    ),
            )
    }

    @Test
    fun onRefresh_forecastStateIsUpdatedCorrectly() =
        runTest {
            val expected =
                ForecastState(
                    persistentListOf(
                        CommonForecast(
                            items = List(1) { CommonForecastItem.Example1 }.toPersistentList(),
                            provider = "Lithuanian Hydrometeorological Service under the Ministry of Environment",
                            creationDateTime = CommonForecastItem.Example1.instant,
                            isLoading = false,
                        ),
                    ),
                )
            forecastRepository.onRefresh(CommonPlace.VILNIUS)

            forecastRepository.state.test {
                assertEquals(
                    ForecastState(
                        persistentListOf(
                            CommonForecast(
                                provider = "Lithuanian Hydrometeorological Service under the Ministry of Environment",
                            ),
                        ),
                    ),
                    awaitItem(),
                )
                assertEquals(expected, awaitItem())
            }
        }
}
