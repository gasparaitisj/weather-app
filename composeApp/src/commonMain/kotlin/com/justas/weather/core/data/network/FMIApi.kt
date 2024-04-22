package com.justas.weather.core.data.network

import com.justas.weather.core.data.response.FMIForecastResponse
import com.justas.weather.core.domain.model.CommonForecast
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import nl.adaptivity.xmlutil.serialization.XML

class FMIApi(
    private val httpClient: HttpClient,
    private val xmlUtil: XML,
) : CommonApi {
    override val name: String
        get() = "Finnish Meteorological Institute"

    override suspend fun getForecast(
        latLon: Pair<Double, Double>?,
        name: String?,
    ): CommonForecast =
        httpClient.get {
            url("http://opendata.fmi.fi/wfs")
            parameter("service", "WFS")
            parameter("version", "2.0.0")
            parameter("request", "getFeature")
            parameter(
                "storedquery_id",
                "fmi::forecast::harmonie::surface::point::multipointcoverage",
            )
            parameter("latlon", "${latLon?.first},${latLon?.second}")
            parameter(
                "parameters",
                "temperature,totalcloudcover,humidity,pressure,precipitation1h,winddirection,windgust,windspeedms",
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
