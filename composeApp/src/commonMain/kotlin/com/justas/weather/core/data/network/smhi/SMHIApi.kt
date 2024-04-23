package com.justas.weather.core.data.network.smhi

import com.justas.weather.core.data.network.ForecastApi
import com.justas.weather.core.data.response.SMHIForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonPlace
import com.justas.weather.core.util.double.round
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class SMHIApi(
    private val httpClient: HttpClient,
) : ForecastApi {
    override val name: String
        get() = "Swedish Meteorological and Hydrological Institute"

    override suspend fun getForecast(place: CommonPlace): CommonForecast =
        httpClient.get {
            val lat = place.coordinates.latitude?.round(scale = SCALE)
            val lon = place.coordinates.longitude?.round(scale = SCALE)
            url(
                "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/$lon/lat/$lat/data.json",
            )
        }.body<SMHIForecastResponse>().toModel(this.name)

    companion object {
        private const val SCALE = 6 // If the scale is higher, the network request is rejected.
    }
}
