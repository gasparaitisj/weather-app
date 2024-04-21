package com.justas.weather.core.data.network

import com.justas.weather.core.data.response.OWMForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class OWMApi(
    private val httpClient: HttpClient,
) : CommonApi {
    override val name: String
        get() = "Open Weather"

    override suspend fun getForecast(
        latLon: Pair<Double, Double>?,
        name: String?,
    ): CommonForecast =
        httpClient.get {
            url("https://api.openweathermap.org/data/2.5/forecast")
            parameter("lat", latLon?.first)
            parameter("lon", latLon?.second)
            parameter("appid", "1410ffb955b7a84289e17d672a5b12ff")
            parameter("units", "metric")
        }.body<OWMForecastResponse>().toModel(this.name)
}
