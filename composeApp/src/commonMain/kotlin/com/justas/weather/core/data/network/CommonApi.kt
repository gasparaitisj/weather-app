package com.justas.weather.core.data.network

import com.justas.weather.core.domain.model.CommonForecast

interface CommonApi {
    val name: String

    suspend fun getForecast(
        latLon: Pair<Double, Double>? = null,
        name: String? = null,
    ): CommonForecast
}
