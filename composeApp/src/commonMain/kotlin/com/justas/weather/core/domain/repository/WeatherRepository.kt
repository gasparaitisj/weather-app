package com.justas.weather.core.domain.repository

import com.justas.weather.core.data.network.CommonApi
import com.justas.weather.core.di.ServiceLocator.log
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
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

class WeatherRepository(
    private val api: ImmutableList<CommonApi>,
) {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val supervisor = SupervisorJob()
    private val handler =
        CoroutineExceptionHandler { _, exception ->
            log.e { exception.stackTraceToString() }
        }

    init {
        onRefresh()
    }

    fun onRefresh() {
        _state.update { uiState ->
            uiState.copy(
                forecasts =
                    buildList {
                        val sortedApi = api.sortedBy { it.name }
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
        with(CoroutineScope(Dispatchers.Default + supervisor)) {
            repeat(api.size) { index ->
                launch(handler) {
                    try {
                        loadForecast(api = api[index])
                    } catch (e: Exception) {
                        throw e
                    }
                }
            }
        }
    }

    private suspend fun loadForecast(api: CommonApi) {
        _state.update { uiState ->
            val forecast =
                api.getForecast(
                    latLon = uiState.latLng,
                    name = uiState.name,
                )
            uiState.copy(
                forecasts =
                    uiState
                        .forecasts
                        .map { forecastItem ->
                            if (forecast.provider == forecastItem.provider) {
                                forecast.copy(
                                    isLoading = false,
                                    items =
                                        forecast
                                            .items
                                            .filterUpToDate(),
                                )
                            } else {
                                forecastItem
                            }
                        }
                        .sortedBy { it.provider }
                        .toPersistentList(),
            )
        }
    }

    private fun PersistentList<CommonForecastItem>.filterUpToDate() =
        filter { item ->
            item.instant != null &&
                Clock.System.now() <= item.instant
        }.toPersistentList()
}
