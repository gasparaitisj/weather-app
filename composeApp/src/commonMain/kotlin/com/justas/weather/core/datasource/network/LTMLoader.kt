package com.justas.weather.core.datasource.network

import com.justas.weather.core.entity.LTMForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class LTMLoader(
    private val httpClient: HttpClient,
) {
    suspend fun getForecast(): LTMForecast =
        httpClient.get(
            urlString = "https://api.meteo.lt/v1/places/vilnius/forecasts/long-term",
        ).body()
}
