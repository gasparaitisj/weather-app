package com.justas.weather.core.data.network.nom

import com.justas.weather.core.data.network.ForecastApi
import com.justas.weather.core.data.response.NOMForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonPlace
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class NOMApi(
    private val httpClient: HttpClient,
) : ForecastApi {
    override val name: String
        get() = "The Norwegian Meteorological Institute"

    override suspend fun getForecast(place: CommonPlace): CommonForecast =
        httpClient.get {
            url("https://api.met.no/weatherapi/locationforecast/2.0/compact")
            parameter("lat", place.coordinates.latitude)
            parameter("lon", place.coordinates.longitude)
        }.body<NOMForecastResponse>().toModel(this.name)
}
