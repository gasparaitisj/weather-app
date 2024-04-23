package com.justas.weather.core.data.network.dmi

import com.justas.weather.core.data.network.ForecastApi
import com.justas.weather.core.data.response.DMIForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonPlace
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class DMIApi(
    private val httpClient: HttpClient,
) : ForecastApi {
    override val name: String
        get() = "The Danish Meteorological Institute"

    override suspend fun getForecast(place: CommonPlace): CommonForecast =
        httpClient.get {
            url("https://dmigw.govcloud.dk/v1/forecastedr/collections/harmonie_dini_sf/position")
            parameter("crs", "crs84")
            parameter(
                "coords",
                "POINT(${place.coordinates.longitude} ${place.coordinates.latitude})",
            )
            parameter(
                "parameter-name",
                buildString {
                    append("temperature-0m,")
                    append("fraction-of-cloud-cover,")
                    append("relative-humidity-2m,")
                    append("pressure-sealevel,")
                    append("total-precipitation,")
                    append("wind-dir,")
                    append("gust-wind-speed-10m,")
                    append("wind-speed,")
                    append("precipitation-type")
                },
            )
            parameter("api-key", "980e1130-67cc-412b-896f-f1bcda260d5c")
        }.body<DMIForecastResponse>().toModel(this.name)
}
