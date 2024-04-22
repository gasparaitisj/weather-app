package com.justas.weather.core.data.network

import com.justas.weather.core.data.response.DMIForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class DMIApi(
    private val httpClient: HttpClient,
) : CommonApi {
    override val name: String
        get() = "The Danish Meteorological Institute"

    override suspend fun getForecast(
        latLon: Pair<Double, Double>?,
        name: String?,
    ): CommonForecast =
        httpClient.get {
            url("https://dmigw.govcloud.dk/v1/forecastedr/collections/harmonie_dini_sf/position")
            parameter("coords", "POINT(${latLon?.second} ${latLon?.first})")
            parameter("crs", "crs84")
            parameter(
                "parameter-name",
                "temperature-0m,fraction-of-cloud-cover,relative-humidity-2m,pressure-sealevel,total-precipitation,wind-dir,gust-wind-speed-10m,wind-speed,precipitation-type",
            )
            parameter("api-key", "980e1130-67cc-412b-896f-f1bcda260d5c")
        }.body<DMIForecastResponse>().toModel(this.name)
}
