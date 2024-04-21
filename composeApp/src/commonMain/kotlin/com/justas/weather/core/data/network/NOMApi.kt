package com.justas.weather.core.data.network

import com.justas.weather.core.data.response.NOMForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class NOMApi(
    private val httpClient: HttpClient,
) : CommonApi {
    override val name: String
        get() = "The Norwegian Meteorological Institute"

    override suspend fun getForecast(
        latLon: Pair<Double, Double>?,
        name: String?,
    ): CommonForecast =
        httpClient.get {
            url("https://api.met.no/weatherapi/locationforecast/2.0/compact")
            parameter("lat", latLon?.first)
            parameter("lon", latLon?.second)
        }.body<NOMForecastResponse>().toModel(this.name)
}
