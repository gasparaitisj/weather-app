package com.justas.weather.core.data.network

import com.justas.weather.core.data.response.SMHIForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.util.double.round
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class SMHIApi(
    private val httpClient: HttpClient,
) : CommonApi {
    override val name: String
        get() = "Swedish Meteorological and Hydrological Institute"

    override suspend fun getForecast(
        latLon: Pair<Double, Double>?,
        name: String?,
    ): CommonForecast =
        httpClient.get {
            val lat = latLon?.first?.round(scale = 6)
            val lon = latLon?.second?.round(scale = 6)
            url(
                "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/$lon/lat/$lat/data.json",
            )
        }.body<SMHIForecastResponse>().toModel(this.name)
}
