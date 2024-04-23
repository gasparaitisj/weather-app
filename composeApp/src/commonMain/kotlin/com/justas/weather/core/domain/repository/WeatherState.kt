package com.justas.weather.core.domain.repository

import com.justas.weather.core.domain.model.CommonForecast
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class WeatherState(
    val forecasts: PersistentList<CommonForecast> = persistentListOf(),
)
