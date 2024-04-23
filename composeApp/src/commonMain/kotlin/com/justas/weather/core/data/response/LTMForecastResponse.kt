package com.justas.weather.core.data.response

import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LTMForecastResponse(
    @SerialName("forecastCreationTimeUtc")
    val forecastCreationTimeUtc: String?,
    @SerialName("forecastTimestamps")
    val forecastTimestamps: List<LTMForecastTimestamp?>?,
    @SerialName("forecastType")
    val forecastType: String?,
    @SerialName("place")
    val place: LTMPlace?
) {
    fun toModel(provider: String): CommonForecast {
        return CommonForecast(
            items =
                forecastTimestamps?.map { forecastTimestamp ->
                    CommonForecastItem(
                        airTemperature = forecastTimestamp?.airTemperature,
                        cloudCover = forecastTimestamp?.cloudCover,
                        condition = forecastTimestamp?.conditionCode.orEmpty(),
                        feelsLikeTemperature = forecastTimestamp?.feelsLikeTemperature,
                        instant = forecastTimestamp?.forecastTimeUtc.toInstantOrNull(),
                        relativeHumidity = forecastTimestamp?.relativeHumidity,
                        seaLevelPressure = forecastTimestamp?.seaLevelPressure,
                        totalPrecipitation = forecastTimestamp?.totalPrecipitation,
                        windDirection = forecastTimestamp?.windDirection,
                        windGust = forecastTimestamp?.windGust,
                        windSpeed = forecastTimestamp?.windSpeed,
                    )
                }.orEmpty().toPersistentList(),
            place = place?.name.orEmpty(),
            provider = provider,
        )
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    private fun String?.toInstantOrNull(): Instant? {
        return this?.let { str ->
            try {
                LocalDateTime.Format {
                    byUnicodePattern("yyyy-MM-dd' 'HH:mm:ss[.SSS]")
                }.parse(str).toInstant(TimeZone.UTC)
            } catch (e: Throwable) {
                null
            }
        }
    }
}

@Serializable
data class LTMForecastTimestamp(
    @SerialName("airTemperature")
    val airTemperature: Double?,
    @SerialName("cloudCover")
    val cloudCover: Double?,
    @SerialName("conditionCode")
    val conditionCode: String?,
    @SerialName("feelsLikeTemperature")
    val feelsLikeTemperature: Double?,
    @SerialName("forecastTimeUtc")
    val forecastTimeUtc: String?,
    @SerialName("relativeHumidity")
    val relativeHumidity: Double?,
    @SerialName("seaLevelPressure")
    val seaLevelPressure: Double?,
    @SerialName("totalPrecipitation")
    val totalPrecipitation: Double?,
    @SerialName("windDirection")
    val windDirection: Double?,
    @SerialName("windGust")
    val windGust: Double?,
    @SerialName("windSpeed")
    val windSpeed: Double?
)

@Serializable
data class LTMPlace(
    @SerialName("administrativeDivision")
    val administrativeDivision: String?,
    @SerialName("code")
    val code: String?,
    @SerialName("coordinates")
    val coordinates: LTMCoordinates?,
    @SerialName("country")
    val country: String?,
    @SerialName("countryCode")
    val countryCode: String?,
    @SerialName("name")
    val name: String?
)

@Serializable
data class LTMCoordinates(
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("longitude")
    val longitude: Double?
)
