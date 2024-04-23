package com.justas.weather.app.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.repository.ForecastRepository
import com.justas.weather.core.domain.repository.ForecastState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

data class InfoState(
    val averageForecastItems: PersistentList<CommonForecastItem> = persistentListOf()
)

class InfoViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val _state = MutableStateFlow(InfoState())
    val state = _state.asStateFlow()

    private val forecastState: ForecastState
        get() = forecastRepository.state.value

    fun onRefresh() {
        viewModelScope.launch {
            val itemMap = forecastState.forecasts.getInstantMap()
            val averageItems: List<CommonForecastItem> =
                itemMap.map { (key, value) ->
                    var averageItem = CommonForecastItem(instant = key)
                    value.map { item ->
                        averageItem =
                            averageItem.copy(
                                airTemperature =
                                    averageItem.airTemperature?.safePlus(item.airTemperature),
                                cloudCover = averageItem.cloudCover?.safePlus(item.cloudCover),
                                feelsLikeTemperature =
                                    averageItem.feelsLikeTemperature?.safePlus(
                                        item.feelsLikeTemperature,
                                    ),
                                relativeHumidity =
                                    averageItem.relativeHumidity?.safePlus(
                                        item.relativeHumidity,
                                    ),
                                seaLevelPressure =
                                    averageItem.seaLevelPressure?.safePlus(
                                        item.seaLevelPressure,
                                    ),
                                totalPrecipitation =
                                    averageItem.totalPrecipitation?.safePlus(
                                        item.totalPrecipitation,
                                    ),
                                windDirection =
                                    averageItem.windDirection?.safePlus(
                                        item.windDirection,
                                    ),
                                windGust = averageItem.windGust?.safePlus(item.windGust),
                                windSpeed = averageItem.windSpeed?.safePlus(item.windSpeed),
                            )
                    }
                    averageItem
                }
            _state.update { uiState ->
                uiState.copy(
                    averageForecastItems = averageItems.toPersistentList(),
                )
            }
        }
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

    private fun Double?.safePlus(other: Double?): Double? = this?.plus(other ?: 0.0)
}
