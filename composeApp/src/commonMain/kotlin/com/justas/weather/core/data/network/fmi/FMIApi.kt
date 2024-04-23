package com.justas.weather.core.data.network.fmi

import com.justas.weather.core.data.network.ForecastApi
import com.justas.weather.core.data.response.FMIForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonPlace
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import nl.adaptivity.xmlutil.serialization.XML

class FMIApi(
    private val httpClient: HttpClient,
    private val xmlUtil: XML,
) : ForecastApi {
    override val name: String
        get() = "Finnish Meteorological Institute"

    override suspend fun getForecast(place: CommonPlace): CommonForecast =
        httpClient.get {
            url("http://opendata.fmi.fi/wfs")
            parameter("service", "WFS")
            parameter("version", "2.0.0")
            parameter("request", "getFeature")
            parameter(
                "storedquery_id",
                "fmi::forecast::harmonie::surface::point::multipointcoverage",
            )
            parameter("latlon", "${place.coordinates.latitude},${place.coordinates.longitude}")
            parameter(
                "parameters",
                buildString {
                    append("temperature,")
                    append("totalcloudcover,")
                    append("humidity,")
                    append("pressure,")
                    append("precipitation1h,")
                    append("winddirection,")
                    append("windgust,")
                    append("windspeedms")
                },
            )
        }.run {
            val body = bodyAsText()
            xmlUtil
                .decodeFromString(
                    deserializer = FMIForecastResponse.serializer(),
                    string = body,
                )
                .toModel(this@FMIApi.name)
        }
}
