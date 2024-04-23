package com.justas.weather.core.domain.repository

import com.justas.weather.core.data.network.ForecastApi
import com.justas.weather.core.di.ServiceLocator.log
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.CommonPlace
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class ForecastRepository(
    private val forecastApis: ImmutableList<ForecastApi>,
) {
    private val _state = MutableStateFlow(WeatherState())
    val state = _state.asStateFlow()

    private val supervisor = SupervisorJob()
    private val exceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            log.e { exception.stackTraceToString() }
        }

    fun onRefresh() {
        initializeForecastsByApiName()
        fetchAllForecasts(CommonPlace.VILNIUS)
    }

    private fun initializeForecastsByApiName() {
        _state.update { uiState ->
            uiState.copy(
                forecasts =
                    buildList {
                        val sortedApi = forecastApis.sortedBy { it.name }
                        repeat(sortedApi.size) { index ->
                            add(
                                CommonForecast(
                                    provider = sortedApi[index].name,
                                    isLoading = true,
                                ),
                            )
                        }
                    }.toPersistentList(),
            )
        }
    }

    private fun fetchAllForecasts(place: CommonPlace) {
        with(CoroutineScope(Dispatchers.Default + supervisor)) {
            repeat(forecastApis.size) { index ->
                launch(exceptionHandler) {
                    loadForecast(
                        api = forecastApis[index],
                        place = place,
                    )
                }
            }
        }
    }

    private suspend fun loadForecast(
        api: ForecastApi,
        place: CommonPlace
    ) {
        _state.update { uiState ->
            val updatedForecast = api.getForecast(place)
            uiState.copy(
                forecasts =
                    uiState
                        .forecasts
                        .updateForecast(updatedForecast)
                        .sortedBy { forecast -> forecast.provider }
                        .toPersistentList(),
            )
        }
    }

    private fun PersistentList<CommonForecast>.updateForecast(updatedForecast: CommonForecast) =
        map { forecast ->
            if (updatedForecast.provider == forecast.provider) {
                updatedForecast.copy(
                    isLoading = false,
                    items =
                        updatedForecast
                            .items
                            .filterUpToDate(),
                )
            } else {
                forecast
            }
        }.toPersistentList()

    private fun PersistentList<CommonForecastItem>.filterUpToDate() =
        filter { item ->
            item.instant != null &&
                Clock.System.now() <= item.instant
        }.toPersistentList()
}
