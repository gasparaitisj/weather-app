package com.justas.weather.core.entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NOMForecast(
    @SerialName("geometry")
    val geometry: Geometry?,
    @SerialName("properties")
    val properties: Properties?,
    @SerialName("type")
    val type: String?
)

@Serializable
internal data class Geometry(
    @SerialName("coordinates")
    val coordinates: List<Double?>?,
    @SerialName("type")
    val type: String?
)

@Serializable
internal data class Properties(
    @SerialName("meta")
    val meta: Meta?,
    @SerialName("timeseries")
    val timeSeries: List<TimeSeries>?
)

@Serializable
internal data class Meta(
    @SerialName("units")
    val units: Units?,
    @SerialName("updated_at")
    val updatedAt: String?
)

@Serializable
internal data class TimeSeries(
    @SerialName("data")
    val `data`: Data?,
    @SerialName("time")
    val time: String?
)

@Serializable
internal data class Units(
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: String?,
    @SerialName("air_temperature")
    val airTemperature: String?,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: String?,
    @SerialName("precipitation_amount")
    val precipitationAmount: String?,
    @SerialName("relative_humidity")
    val relativeHumidity: String?,
    @SerialName("wind_from_direction")
    val windFromDirection: String?,
    @SerialName("wind_speed")
    val windSpeed: String?
)

@Serializable
internal data class Data(
    @SerialName("instant")
    val instant: Instant?,
    @SerialName("next_12_hours")
    val next12Hours: Next12Hours?,
    @SerialName("next_1_hours")
    val next1Hours: Next1Hours?,
    @SerialName("next_6_hours")
    val next6Hours: Next6Hours?
)

@Serializable
internal data class Instant(
    @SerialName("details")
    val details: Details?
)

@Serializable
internal data class Next12Hours(
    @SerialName("details")
    val details: Details?,
    @SerialName("summary")
    val summary: Summary?
)

@Serializable
internal data class Next1Hours(
    @SerialName("details")
    val details: Details?,
    @SerialName("summary")
    val summary: Summary?
)

@Serializable
internal data class Next6Hours(
    @SerialName("details")
    val details: Details?,
    @SerialName("summary")
    val summary: Summary?
)

@Serializable
internal data class Details(
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: Double?,
    @SerialName("air_temperature")
    val airTemperature: Double?,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: Double?,
    @SerialName("relative_humidity")
    val relativeHumidity: Double?,
    @SerialName("wind_from_direction")
    val windFromDirection: Double?,
    @SerialName("wind_speed")
    val windSpeed: Double?
)

@Serializable
internal data class Summary(
    @SerialName("symbol_code")
    val symbolCode: String?
)
