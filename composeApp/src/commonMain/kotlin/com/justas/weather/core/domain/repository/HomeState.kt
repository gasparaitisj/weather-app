package com.justas.weather.core.domain.repository

import com.justas.weather.core.domain.model.CommonForecast
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class HomeState(
    val forecasts: PersistentList<CommonForecast> = persistentListOf(),
    val latLng: Pair<Double, Double> = 54.68906448528366 to 25.282551533212363,
    val name: String = "vilnius",
)
