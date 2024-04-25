package com.justas.weather.app.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.repository.ForecastRepository
import com.justas.weather.core.domain.repository.ForecastState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

class InfoViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val _state = MutableStateFlow(InfoState())
    val state = _state.asStateFlow()

    private val forecastState: ForecastState
        get() = forecastRepository.state.value

    fun onRefresh() {
        viewModelScope.launch {
            val averageItems =
                forecastState
                    .forecasts
                    .getInstantMap()
                    .getAverageItems()
                    .toPersistentList()
            _state.update { uiState ->
                uiState.copy(
                    averageForecastItems = averageItems,
                )
            }
        }
    }

    private fun Map<Instant, List<CommonForecastItem>>.getAverageItems(): List<CommonForecastItem> =
        buildList {
            this@getAverageItems.map { mapEntry ->
                val averageItem = mapEntry.toPair().averageItem()
                add(averageItem)
            }
        }

    private fun Pair<Instant, List<CommonForecastItem>>.averageItem(): CommonForecastItem {
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

    private fun PersistentList<CommonForecast>.getInstantMap(): Map<Instant, List<CommonForecastItem>> =
        buildMap {
            this@getInstantMap.forEach { forecast ->
                forecast.items
                    .groupBy { item -> item.instant }
                    .forEach { (key, value) ->
                        if (key != null) {
                            this[key] = this[key]?.plus(value) ?: value
                        }
                    }
            }
        }

    private fun Double?.safePlus(second: Double?): Double = this?.plus(second ?: 0.0) ?: 0.0
}
