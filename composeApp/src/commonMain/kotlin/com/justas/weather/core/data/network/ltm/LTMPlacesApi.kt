package com.justas.weather.core.data.network.ltm

import com.justas.weather.core.data.response.LTMPlacesResponseItem
import com.justas.weather.core.domain.model.CommonPlace
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class LTMPlacesApi(
    private val httpClient: HttpClient,
) {
    suspend fun getPlaces(): List<CommonPlace> =
        httpClient.get {
            url("$BASE_URL/places")
        }.body<List<LTMPlacesResponseItem>>().map { it.toModel() }

    suspend fun getPlaceByCode(code: String): CommonPlace =
        httpClient.get {
            url("$BASE_URL/places/$code")
        }.body<LTMPlacesResponseItem>().toModel()

    companion object {
        private const val BASE_URL = "https://api.meteo.lt/v1"
    }
}
