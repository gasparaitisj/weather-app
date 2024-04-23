package com.justas.weather.core.data.network.ltm

import com.justas.weather.core.data.network.ForecastApi
import com.justas.weather.core.data.response.LTMForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonPlace
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.encodeURLPath

class LTMForecastApi(
    private val httpClient: HttpClient,
) : ForecastApi {
    override val name: String
        get() = "Lithuanian Hydrometeorological Service under the Ministry of Environment"

    override suspend fun getForecast(place: CommonPlace): CommonForecast =
        httpClient.get {
            url("https://api.meteo.lt/v1/places/${place.code}/forecasts/long-term".encodeURLPath())
        }.body<LTMForecastResponse>().toModel(this.name)
}
