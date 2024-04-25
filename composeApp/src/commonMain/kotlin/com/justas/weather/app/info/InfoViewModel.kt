package com.justas.weather.app.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.repository.ForecastRepository
import com.justas.weather.core.domain.repository.ForecastState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class InfoViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val _state = MutableStateFlow(InfoState())
    val state = _state.asStateFlow()

    private val forecastState: ForecastState
        get() = forecastRepository.state.value

    init {
        onCreated()
    }

    private fun onCreated() {
        collectAndUpdateIfAllForecastsAreLoaded()
    }

    private fun collectAndUpdateIfAllForecastsAreLoaded() {
        viewModelScope.launch {
            forecastRepository.state.collectLatest { state ->
                val isAnyForecastLoading = state.forecasts.any { it.isLoading }
                _state.update { uiState ->
                    uiState.copy(
                        isLoading = isAnyForecastLoading,
                    )
                }
                if (!isAnyForecastLoading) {
                    onRefresh()
                }
            }
        }
    }

    private fun onRefresh() {
        val averageItemsByHour =
            forecastState
                .forecasts
                .getInstantMap()
                .getAverageItemsByHour()
                .toPersistentList()
        val averageItemsByDay = averageItemsByHour.getAverageItemsByDay()
        _state.update { uiState ->
            uiState.copy(
                isLoading = false,
                averageForecastItemsByHour = averageItemsByHour,
                averageForecastItemsByDay = averageItemsByDay,
            )
        }
    }

    private fun PersistentList<CommonForecastItem>.getAverageItemsByDay(): PersistentList<Pair<LocalDate, PersistentList<CommonForecastItem>>> =
        groupBy { item ->
            item.instant?.toLocalDateTime(
                TimeZone.currentSystemDefault(),
            )?.date ?: LocalDate.fromEpochDays(0)
        }.filter { (key, _) ->
            key != LocalDate.fromEpochDays(0)
        }.map { (key, value) ->
            key to value.toPersistentList()
        }.toList().toPersistentList()

    private fun PersistentMap<Instant, PersistentList<CommonForecastItem>>.getAverageItemsByHour(): PersistentList<CommonForecastItem> =
        buildList {
            this@getAverageItemsByHour.forEach { mapEntry ->
                val averageItem = mapEntry.toPair().averageItem()
                add(averageItem)
            }
        }.toPersistentList()

    private fun Pair<Instant, PersistentList<CommonForecastItem>>.averageItem(): CommonForecastItem {
        var averageItem = CommonForecastItem.WithZeroes.copy(instant = first)
        second.forEach { item ->
            averageItem =
                averageItem.copy(
                    airTemperature =
                        averageItem.airTemperature?.safePlus(item.airTemperature),
                    cloudCover =
                        averageItem.cloudCover?.safePlus(item.cloudCover),
                    feelsLikeTemperature =
                        averageItem.feelsLikeTemperature?.safePlus(item.feelsLikeTemperature),
                    relativeHumidity =
                        averageItem.relativeHumidity?.safePlus(item.relativeHumidity),
                    seaLevelPressure =
                        averageItem.seaLevelPressure?.safePlus(item.seaLevelPressure),
                    totalPrecipitation =
                        averageItem.totalPrecipitation?.safePlus(item.totalPrecipitation),
                    windDirection =
                        averageItem.windDirection?.safePlus(item.windDirection),
                    windGust =
                        averageItem.windGust?.safePlus(item.windGust),
                    windSpeed =
                        averageItem.windSpeed?.safePlus(item.windSpeed),
                )
        }
        return averageItem.copy(
            airTemperature =
                averageItem.airTemperature?.div(second.size),
            cloudCover =
                averageItem.cloudCover?.div(second.size),
            feelsLikeTemperature =
                averageItem.feelsLikeTemperature?.div(second.size),
            relativeHumidity =
                averageItem.relativeHumidity?.div(second.size),
            seaLevelPressure =
                averageItem.seaLevelPressure?.div(second.size),
            totalPrecipitation =
                averageItem.totalPrecipitation?.div(second.size),
            windDirection =
                averageItem.windDirection?.div(second.size),
            windGust =
                averageItem.windGust?.div(second.size),
            windSpeed =
                averageItem.windSpeed?.div(second.size),
        )
    }

    private fun PersistentList<CommonForecast>.getInstantMap(): PersistentMap<Instant, PersistentList<CommonForecastItem>> =
        buildMap<Instant, PersistentList<CommonForecastItem>> {
            this@getInstantMap.forEach { forecast ->
                forecast.items
                    .groupBy { item -> item.instant }
                    .forEach { (key, value) ->
                        if (key != null) {
                            val list = this[key] ?: persistentListOf()
                            this[key] = list.plus(value)
                        }
                    }
            }
        }.toPersistentMap()

    private fun Double?.safePlus(second: Double?): Double = this?.plus(second ?: 0.0) ?: 0.0
}
