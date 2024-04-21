package com.justas.weather.core.data.network

import com.justas.weather.core.data.response.LTMForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class LTMApi(
    private val httpClient: HttpClient,
) : CommonApi {
    override val name: String
        get() = "Lithuanian Hydrometeorological Service under the Ministry of Environment"

    override suspend fun getForecast(
        latLon: Pair<Double, Double>?,
        name: String?,
    ): CommonForecast =
        httpClient.get {
            url("https://api.meteo.lt/v1/places/$name/forecasts/long-term")
        }.body<LTMForecastResponse>().toModel(this.name)
}
