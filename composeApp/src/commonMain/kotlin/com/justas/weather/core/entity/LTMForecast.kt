package com.justas.weather.core.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LTMForecast(
    @SerialName("forecastCreationTimeUtc")
    val forecastCreationTimeUtc: String?,
    @SerialName("forecastTimestamps")
    val forecastTimestamps: List<ForecastTimestamp?>?,
    @SerialName("forecastType")
    val forecastType: String?,
    @SerialName("place")
    val place: Place?
)

@Serializable
internal data class ForecastTimestamp(
    @SerialName("airTemperature")
    val airTemperature: Double?,
    @SerialName("cloudCover")
    val cloudCover: Int?,
    @SerialName("conditionCode")
    val conditionCode: String?,
    @SerialName("feelsLikeTemperature")
    val feelsLikeTemperature: Double?,
    @SerialName("forecastTimeUtc")
    val forecastTimeUtc: String?,
    @SerialName("relativeHumidity")
    val relativeHumidity: Int?,
    @SerialName("seaLevelPressure")
    val seaLevelPressure: Int?,
    @SerialName("totalPrecipitation")
    val totalPrecipitation: Double?,
    @SerialName("windDirection")
    val windDirection: Int?,
    @SerialName("windGust")
    val windGust: Int?,
    @SerialName("windSpeed")
    val windSpeed: Int?
)

@Serializable
internal data class Place(
    @SerialName("administrativeDivision")
    val administrativeDivision: String?,
    @SerialName("code")
    val code: String?,
    @SerialName("coordinates")
    val coordinates: Coordinates?,
    @SerialName("country")
    val country: String?,
    @SerialName("countryCode")
    val countryCode: String?,
    @SerialName("name")
    val name: String?
)

@Serializable
internal data class Coordinates(
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("longitude")
    val longitude: Double?
)
