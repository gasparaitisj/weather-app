package com.justas.weather.core.data.response
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NOMForecastResponse(
    @SerialName("geometry")
    val geometry: NOMGeometry?,
    @SerialName("properties")
    val properties: NOMProperties?,
    @SerialName("type")
    val type: String?
) {
    fun toModel(provider: String): CommonForecast {
        return CommonForecast(
            items =
                properties?.timeSeries?.map { timeSeries ->
                    val instantDetails = timeSeries.data?.instant?.details
                    CommonForecastItem(
                        airTemperature = instantDetails?.airTemperature,
                        cloudCover = instantDetails?.cloudAreaFraction,
                        condition = timeSeries.data?.next1Hours?.summary?.symbolCode.orEmpty(),
                        instant = timeSeries.time.toInstantOrNull(),
                        relativeHumidity = instantDetails?.relativeHumidity,
                        seaLevelPressure = instantDetails?.airPressureAtSeaLevel,
                        totalPrecipitation =
                            timeSeries.data?.next1Hours?.details?.precipitationAmount,
                        windDirection = instantDetails?.windFromDirection,
                        windSpeed = instantDetails?.windSpeed,
                    )
                }.orEmpty().toPersistentList(),
            provider = provider,
        )
    }

    private fun String?.toInstantOrNull(): Instant? {
        return this
            ?.replace("Z", "") // Always a UTC string, so we remove the zone.
            ?.let { utcString ->
                try {
                    LocalDateTime
                        .parse(utcString)
                        .toInstant(TimeZone.UTC)
                } catch (e: Throwable) {
                    throw e
                }
            }
    }
}

@Serializable
data class NOMGeometry(
    @SerialName("coordinates")
    val coordinates: List<Double?>? = null,
    @SerialName("type")
    val type: String? = null,
)

@Serializable
data class NOMProperties(
    @SerialName("meta")
    val meta: NOMMeta? = null,
    @SerialName("timeseries")
    val timeSeries: List<NOMTimeSeries>? = null,
)

@Serializable
data class NOMMeta(
    @SerialName("units")
    val units: NOMUnits? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
)

@Serializable
data class NOMTimeSeries(
    @SerialName("data")
    val data: NOMData? = null,
    @SerialName("time")
    val time: String? = null,
)

@Serializable
data class NOMUnits(
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: String? = null,
    @SerialName("air_temperature")
    val airTemperature: String? = null,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: String? = null,
    @SerialName("precipitation_amount")
    val precipitationAmount: String? = null,
    @SerialName("relative_humidity")
    val relativeHumidity: String? = null,
    @SerialName("wind_from_direction")
    val windFromDirection: String? = null,
    @SerialName("wind_speed")
    val windSpeed: String? = null,
)

@Serializable
data class NOMData(
    @SerialName("instant")
    val instant: NOMDetailsSummary? = null,
    @SerialName("next_12_hours")
    val next12Hours: NOMDetailsSummary? = null,
    @SerialName("next_1_hours")
    val next1Hours: NOMDetailsSummary? = null,
    @SerialName("next_6_hours")
    val next6Hours: NOMDetailsSummary? = null,
)

@Serializable
data class NOMDetailsSummary(
    @SerialName("details")
    val details: NOMDetails? = null,
    @SerialName("summary")
    val summary: NOMSummary? = null,
)

@Serializable
data class NOMDetails(
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: Double? = null,
    @SerialName("air_temperature")
    val airTemperature: Double? = null,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: Double? = null,
    @SerialName("precipitation_amount")
    val precipitationAmount: Double? = null,
    @SerialName("relative_humidity")
    val relativeHumidity: Double? = null,
    @SerialName("wind_from_direction")
    val windFromDirection: Double? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null,
)

@Serializable
data class NOMSummary(
    @SerialName("symbol_code")
    val symbolCode: String? = null,
)
