package com.justas.weather.core.data.network

import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonPlace

interface ForecastApi {
    val name: String

    suspend fun getForecast(place: CommonPlace): CommonForecast
}
