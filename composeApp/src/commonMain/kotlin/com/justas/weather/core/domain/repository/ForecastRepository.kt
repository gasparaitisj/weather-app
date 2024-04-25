package com.justas.weather.core.domain.repository

import com.justas.weather.core.data.network.ForecastApi
import com.justas.weather.core.di.ServiceLocator.log
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.CommonPlace
import com.justas.weather.core.util.instant.isAfter
import kotlin.time.Duration.Companion.hours
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForecastRepository(
    private val forecastApis: ImmutableList<ForecastApi>,
) {
    private val _state = MutableStateFlow(ForecastState())
    val state = _state.asStateFlow()

    private val supervisor = SupervisorJob()
    private val exceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            log.e { exception.stackTraceToString() }
        }

    fun onRefresh(place: CommonPlace) {
        if (place.validity.isNotEmpty()) return
        initializeForecastsByApiName()
        fetchAllForecasts(place)
    }

    private val CommonPlace.validity get(): String {
        return if (name.isBlank()) {
            "The place is invalid"
        } else {
            ""
        }
    }

    private fun initializeForecastsByApiName() {
        _state.update { uiState ->
            uiState.copy(
                forecasts =
                    buildList {
                        val sortedApi =
                            forecastApis
                                .sortedBy { it.name }
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
                }.invokeOnCompletion(getCompletionHandler(forecastApis[index].name))
            }
        }
    }

    private fun getCompletionHandler(provider: String): CompletionHandler =
        { cause: Throwable? ->
            if (cause == null) {
                log.i { "$provider loaded successfully!" }
            } else {
                log.e { "Error while loading $provider forecast. ${cause.stackTraceToString()}" }
                _state.update { uiState ->
                    val forecast =
                        CommonForecast(
                            isLoading = false,
                            provider = provider,
                            errorMessage = cause.stackTraceToString(),
                        )
                    uiState.copy(
                        forecasts = uiState.forecasts.updateForecast(forecast),
                    )
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
            item.instant != null && item.instant.isAfter((-1).hours)
        }.toPersistentList()
}
