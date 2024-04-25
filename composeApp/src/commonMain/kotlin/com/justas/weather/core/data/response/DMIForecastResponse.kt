package com.justas.weather.core.data.response

import com.ionspin.kotlin.bignum.decimal.toBigDecimal
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
data class DMIForecastResponse(
    @SerialName("domain")
    val domain: DMIDomain?,
    @SerialName("ranges")
    val ranges: DMIRanges?,
) {
    fun toModel(provider: String): CommonForecast {
        val timestampValues = domain?.axes?.timeStamps?.values.orEmpty()
        val cloudCoverValues = ranges?.fractionOfCloudCover?.values.orEmpty()
        val windGustValues = ranges?.gustWindSpeed10m?.values.orEmpty()
        val seaLevelPressureValues = ranges?.pressureSealevel?.values.orEmpty()
        val relativeHumidityValues = ranges?.relativeHumidity2m?.values.orEmpty()
        val temperatureValues = ranges?.temperature0m?.values.orEmpty()
        val totalPrecipitationValues = ranges?.totalPrecipitation?.values.orEmpty()
        val windDirectionValues = ranges?.windDir?.values.orEmpty()
        val windSpeedValues = ranges?.windSpeed?.values.orEmpty()
        val precipitationTypeValues = ranges?.precipitationType?.values.orEmpty()

        val items =
            buildList {
                timestampValues.forEachIndexed { index, timestampValue ->
                    val airTemperatureInCelsius = temperatureValues[index]?.minus(273.15)
                    val condition =
                        DMIPrecipitationType.fromInt(
                            precipitationTypeValues[index]?.toInt(),
                        ).description
                    val cloudCoverInPercentage =
                        cloudCoverValues[index]
                            ?.toBigDecimal()
                            ?.times(100)
                            ?.doubleValue(exactRequired = false)
                    val seaLevelPressureInHpa = seaLevelPressureValues[index]?.times(0.01)
                    val windDirection = windDirectionValues[index]
                    add(
                        CommonForecastItem(
                            airTemperature = airTemperatureInCelsius,
                            condition = condition,
                            cloudCover = cloudCoverInPercentage,
                            instant = timestampValue.toInstantOrNull(),
                            relativeHumidity = relativeHumidityValues[index],
                            seaLevelPressure = seaLevelPressureInHpa,
                            totalPrecipitation = totalPrecipitationValues[index],
                            windDirection = windDirection,
                            windGust = windGustValues[index],
                            windSpeed = windSpeedValues[index],
                        ),
                    )
                }
            }.toPersistentList()
        return CommonForecast(
            items = items,
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
data class DMIDomain(
    @SerialName("axes")
    val axes: DMIAxes?,
)

@Serializable
data class DMIRanges(
    @SerialName("fraction-of-cloud-cover")
    val fractionOfCloudCover: DMIStrings?,
    @SerialName("gust-wind-speed-10m")
    val gustWindSpeed10m: DMIDoubles?,
    @SerialName("precipitation-type")
    val precipitationType: DMIDoubles?,
    @SerialName("pressure-sealevel")
    val pressureSealevel: DMIDoubles?,
    @SerialName("relative-humidity-2m")
    val relativeHumidity2m: DMIDoubles?,
    @SerialName("temperature-0m")
    val temperature0m: DMIDoubles?,
    @SerialName("total-precipitation")
    val totalPrecipitation: DMIDoubles?,
    @SerialName("wind-dir")
    val windDir: DMIDoubles?,
    @SerialName("wind-speed")
    val windSpeed: DMIDoubles?
)

@Serializable
data class DMIAxes(
    @SerialName("t")
    val timeStamps: DMIStrings?,
)

@Serializable
data class DMIDoubles(
    @SerialName("values")
    val values: List<Double?>?
)

@Serializable
data class DMIStrings(
    @SerialName("values")
    val values: List<String?>?
)

enum class DMIPrecipitationType(
    val description: String
) {
    NO_PRECIPITATION("No precipitation"),
    RAIN("Rain"),
    FREEZING_RAIN("Freezing rain"),
    SNOW("Snow"),
    WET_SNOW("Wet snow"),
    RAIN_AND_SNOW_MIX("Mixture of rain and snow"),
    ICE_PELLETS("Ice pellets"),
    FREEZING_DRIZZLE("Freezing drizzle"),
    UNKNOWN("Not available");

    companion object {
        fun fromInt(value: Int?): DMIPrecipitationType {
            return when (value) {
                0 -> NO_PRECIPITATION
                1 -> RAIN
                3 -> FREEZING_RAIN
                5 -> SNOW
                6 -> WET_SNOW
                7 -> RAIN_AND_SNOW_MIX
                8 -> ICE_PELLETS
                12 -> FREEZING_DRIZZLE
                else -> UNKNOWN
            }
        }
    }
}
