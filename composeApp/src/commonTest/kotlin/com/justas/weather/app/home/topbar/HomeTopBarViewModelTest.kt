package com.justas.weather.app.home.topbar

import app.cash.turbine.test
import com.justas.weather.core.data.network.ltm.LTMPlacesApi
import com.justas.weather.core.di.ServiceLocator
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest

class HomeTopBarViewModelTest {
    companion object {
        private val getPlacesResponse =
            """
            [
                {
                    "code": "abromiskes",
                    "name": "Abromiškės",
                    "administrativeDivision": "Elektrėnų savivaldybė",
                    "countryCode": "LT"
                }
            ]
            """.trimIndent()
        private val getPlaceByCodeResponse =
            """
            {
                "code": "vilnius",
                "name": "Vilnius",
                "administrativeDivision": "Vilniaus miesto savivaldybė",
                "country": "Lietuva",
                "countryCode": "LT",
                "coordinates": {
                    "latitude": 54.687046,
                    "longitude": 25.282911
                }
            }
            """.trimIndent()
        private val mockEngine =
            MockEngine { request ->
                respond(
                    content =
                        ByteReadChannel(
                            when (request.url.toString()) {
                                "https://api.meteo.lt/v1/places" -> {
                                    getPlacesResponse
                                }
                                else -> {
                                    getPlaceByCodeResponse
                                }
                            },
                        ),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        private val httpClient = ServiceLocator.createHttpClient(mockEngine)
        private val viewModel =
            HomeTopBarViewModel(
                placesApi = LTMPlacesApi(httpClient),
                forecastRepository = ForecastRepository(persistentListOf()),
            )
    }

    @Test
    fun onGetPlaces_placesAreLoadedCorrectly(): TestResult =
        runTest {
            var expected = HomeTopBarState()
            viewModel.getPlaces()

            viewModel.state.test {
                assertEquals(awaitItem(), expected)

                expected =
                    HomeTopBarState(
                        places =
                            persistentListOf(
                                CommonPlace(
                                    administrativeDivision = "Elektrėnų savivaldybė",
                                    code = "abromiskes",
                                    countryCode = "LT",
                                    name = "Abromiškės",
                                ),
                            ),
                        isLoading = false,
                    )
                assertEquals(awaitItem(), expected)
            }
        }

    @Test
    fun onPlaceSelected_placeIsSelectedCorrectly(): TestResult =
        runTest {
            var expected = HomeTopBarState()

            viewModel.onPlaceSelected(CommonPlace.VILNIUS)

            viewModel.state.test {
                assertEquals(expected, awaitItem())

                expected =
                    expected.copy(
                        selectedPlace = CommonPlace.VILNIUS,
                        isLoading = false,
                    )
            }
        }
}
