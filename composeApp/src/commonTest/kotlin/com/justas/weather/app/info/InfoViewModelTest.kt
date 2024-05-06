package com.justas.weather.app.info

import app.cash.turbine.test
import com.justas.weather.core.data.network.ltm.LTMForecastApi
import com.justas.weather.core.di.ServiceLocator
import com.justas.weather.core.domain.model.CommonDailyForecastItem
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.CommonPlace
import com.justas.weather.core.domain.repository.ForecastRepository
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate

class InfoViewModelTest {
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
                        "windDirection": 250,
                        "cloudCover": 48,
                        "seaLevelPressure": 1013,
                        "relativeHumidity": 39,
                        "totalPrecipitation": 0,
                        "conditionCode": "partly-cloudy"
                    },
                    {
                        "forecastTimeUtc": "9999-01-01 02:01:00",
                        "airTemperature": 24.2,
                        "feelsLikeTemperature": 24.2,
                        "windSpeed": 4,
                        "windGust": 9,
                        "windDirection": 124,
                        "cloudCover": 41,
                        "seaLevelPressure": 1008,
                        "relativeHumidity": 56,
                        "totalPrecipitation": 0.2,
                        "conditionCode": "partly-cloudy"
                    },
                    {
                        "forecastTimeUtc": "9999-01-02 01:01:00",
                        "airTemperature": 14.2,
                        "feelsLikeTemperature": 14.2,
                        "windSpeed": 3,
                        "windGust": 8,
                        "windDirection": 250,
                        "cloudCover": 48,
                        "seaLevelPressure": 1013,
                        "relativeHumidity": 39,
                        "totalPrecipitation": 0,
                        "conditionCode": "partly-cloudy"
                    },
                    {
                        "forecastTimeUtc": "9999-01-02 02:01:00",
                        "airTemperature": 24.2,
                        "feelsLikeTemperature": 24.2,
                        "windSpeed": 4,
                        "windGust": 9,
                        "windDirection": 124,
                        "cloudCover": 41,
                        "seaLevelPressure": 1008,
                        "relativeHumidity": 56,
                        "totalPrecipitation": 0.2,
                        "conditionCode": "partly-cloudy"
                    }
                ]
            }
            """.trimIndent()
        private val mockEngine =
            MockEngine { request ->
                respond(
                    content = ByteReadChannel(response),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        private val httpClient = ServiceLocator.createHttpClient(mockEngine)
    }

    @Test
    fun onInit_forecastAggregationWorksCorrectly() =
        runTest {
            val expected =
                InfoState(
                    averageForecastItemsByDay =
                        persistentListOf(
                            Triple(
                                first = LocalDate(9999, 1, 1),
                                second =
                                    persistentListOf(
                                        CommonForecastItem.Example1,
                                        CommonForecastItem.Example2.copy(
                                            instant =
                                                CommonForecastItem.Example1.instant?.plus(1.days),
                                        ),
                                    ),
                                third =
                                    CommonDailyForecastItem(
                                        averageWindDirection =
                                            CommonForecastItem.Example1.windDirection?.div(
                                                CommonForecastItem.Example2.windDirection ?: 0.0,
                                            ),
                                        averageWindGust =
                                            CommonForecastItem.Example1.windGust?.div(
                                                CommonForecastItem.Example2.windGust ?: 0.0,
                                            ),
                                        averageWindSpeed =
                                            CommonForecastItem.Example1.windSpeed?.div(
                                                CommonForecastItem.Example2.windSpeed ?: 0.0,
                                            ),
                                        highAirTemperature = CommonForecastItem.Example2.airTemperature,
                                        highCloudCover = CommonForecastItem.Example2.cloudCover,
                                        highFeelsLikeTemperature = CommonForecastItem.Example2.feelsLikeTemperature,
                                        highRelativeHumidity = CommonForecastItem.Example2.relativeHumidity,
                                        highSeaLevelPressure = CommonForecastItem.Example2.seaLevelPressure,
                                        highTotalPrecipitation = CommonForecastItem.Example2.totalPrecipitation,
                                        lowAirTemperature = CommonForecastItem.Example1.airTemperature,
                                        lowCloudCover = CommonForecastItem.Example1.cloudCover,
                                        lowFeelsLikeTemperature = CommonForecastItem.Example1.feelsLikeTemperature,
                                        lowRelativeHumidity = CommonForecastItem.Example1.relativeHumidity,
                                        lowSeaLevelPressure = CommonForecastItem.Example1.seaLevelPressure,
                                        lowTotalPrecipitation = CommonForecastItem.Example1.totalPrecipitation,
                                    ),
                            ),
                            Triple(
                                first = LocalDate(9999, 1, 2),
                                second =
                                    persistentListOf(
                                        CommonForecastItem.Example1,
                                        CommonForecastItem.Example2.copy(
                                            instant =
                                                CommonForecastItem.Example2.instant?.plus(1.days),
                                        ),
                                    ),
                                third =
                                    CommonDailyForecastItem(
                                        averageWindDirection =
                                            CommonForecastItem.Example1.windDirection?.div(
                                                CommonForecastItem.Example2.windDirection ?: 0.0,
                                            ),
                                        averageWindGust =
                                            CommonForecastItem.Example1.windGust?.div(
                                                CommonForecastItem.Example2.windGust ?: 0.0,
                                            ),
                                        averageWindSpeed =
                                            CommonForecastItem.Example1.windSpeed?.div(
                                                CommonForecastItem.Example2.windSpeed ?: 0.0,
                                            ),
                                        highAirTemperature = CommonForecastItem.Example2.airTemperature,
                                        highCloudCover = CommonForecastItem.Example2.cloudCover,
                                        highFeelsLikeTemperature = CommonForecastItem.Example2.feelsLikeTemperature,
                                        highRelativeHumidity = CommonForecastItem.Example2.relativeHumidity,
                                        highSeaLevelPressure = CommonForecastItem.Example2.seaLevelPressure,
                                        highTotalPrecipitation = CommonForecastItem.Example2.totalPrecipitation,
                                        lowAirTemperature = CommonForecastItem.Example1.airTemperature,
                                        lowCloudCover = CommonForecastItem.Example1.cloudCover,
                                        lowFeelsLikeTemperature = CommonForecastItem.Example1.feelsLikeTemperature,
                                        lowRelativeHumidity = CommonForecastItem.Example1.relativeHumidity,
                                        lowSeaLevelPressure = CommonForecastItem.Example1.seaLevelPressure,
                                        lowTotalPrecipitation = CommonForecastItem.Example1.totalPrecipitation,
                                    ),
                            ),
                        ),
                )
            val forecastRepository =
                ForecastRepository(
                    persistentListOf(
                        LTMForecastApi(httpClient),
                    ),
                )
            forecastRepository.onRefresh(CommonPlace.VILNIUS)
            val viewModel = InfoViewModel(forecastRepository = forecastRepository)
            viewModel.state.test {
                assertEquals(expected, awaitItem())
            }
        }
}
