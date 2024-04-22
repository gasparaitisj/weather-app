package com.justas.weather.core.data.response

import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.WindDirection
import kotlin.math.roundToInt
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.until
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SMHIForecastResponse(
    @SerialName("approvedTime")
    val approvedTime: String?,
    @SerialName("geometry")
    val geometry: SMHIGeometry?,
    @SerialName("referenceTime")
    val referenceTime: String?,
    @SerialName("timeSeries")
    val timeSeries: List<SMHITimeSeries?>?
) {
    fun toModel(name: String): CommonForecast {
        var lastValidTime: Instant? = null
        val items =
            timeSeries?.map { timeSeriesItem ->
                val validTime = timeSeriesItem?.validTime.toInstantOrNull()
                val hoursPassedSinceLastValidTime =
                    getHoursPassedSinceLastValidTime(validTime, lastValidTime)
                lastValidTime = validTime
                val params = timeSeriesItem?.parameters.orEmpty().filterNotNull()
                CommonForecastItem(
                    airTemperature = params.findValue(SMHIParameterType.AIR_TEMPERATURE),
                    cloudCover = params.findValue(SMHIParameterType.MEAN_TOTAL_CLOUD_COVER),
                    condition = params.findWeatherSymbol(),
                    instant = timeSeriesItem?.validTime?.toInstantOrNull(),
                    relativeHumidity = params.findValue(SMHIParameterType.RELATIVE_HUMIDITY),
                    seaLevelPressure = params.findValue(SMHIParameterType.AIR_PRESSURE),
                    totalPrecipitation =
                        params.findValue(
                            SMHIParameterType.MEAN_PRECIPITATION_INTENSITY,
                        )?.div(hoursPassedSinceLastValidTime),
                    windDirection =
                        WindDirection.getDirection(
                            params.findValue(SMHIParameterType.WIND_DIRECTION)?.roundToInt(),
                        ),
                    windGust = params.findValue(SMHIParameterType.WIND_GUST_SPEED),
                    windSpeed = params.findValue(SMHIParameterType.WIND_SPEED),
                )
            }.orEmpty().toPersistentList()
        return CommonForecast(
            items = items,
            provider = name,
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

    private fun getHoursPassedSinceLastValidTime(
        validTime: Instant?,
        lastValidTime: Instant?
    ): Double {
        if (validTime == null || lastValidTime == null) {
            return 1.0
        }
        return lastValidTime.until(
            other = validTime,
            unit = DateTimeUnit.HOUR,
        ).toDouble()
    }

    private fun List<SMHIParameter>.findValue(type: SMHIParameterType,): Double? =
        find { param ->
            SMHIParameterType.getFromString(param.name) == type
        }?.values?.firstOrNull()

    private fun List<SMHIParameter>.findWeatherSymbol(): String {
        val code =
            find { param ->
                SMHIParameterType.getFromString(param.name) == SMHIParameterType.WEATHER_SYMBOL
            }?.values?.firstOrNull()?.toInt() ?: return ""
        return SMHIParameterSymbolType.fromCode(code)?.meaning.orEmpty()
    }
}

@Serializable
data class SMHIGeometry(
    @SerialName("coordinates")
    val coordinates: List<List<Double?>?>?,
    @SerialName("type")
    val type: String?
)

@Serializable
data class SMHITimeSeries(
    @SerialName("parameters")
    val parameters: List<SMHIParameter?>?,
    @SerialName("validTime")
    val validTime: String?
)

@Serializable
data class SMHIParameter(
    @SerialName("level")
    val level: Int?,
    @SerialName("levelType")
    val levelType: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("values")
    val values: List<Double?>?
)

enum class SMHIParameterType(
    val value: String
) {
    AIR_PRESSURE("msl"),
    AIR_TEMPERATURE("t"),
    HORIZONTAL_VISIBILITY("vis"),
    WIND_DIRECTION("wd"),
    WIND_SPEED("ws"),
    RELATIVE_HUMIDITY("r"),
    THUNDER_PROBABILITY("tstm"),
    MEAN_TOTAL_CLOUD_COVER("tcc_mean"),
    MEAN_LOW_LEVEL_CLOUD_COVER("lcc_mean"),
    MEAN_MEDIUM_LEVEL_CLOUD_COVER("mcc_mean"),
    MEAN_HIGH_LEVEL_CLOUD_COVER("hcc_mean"),
    WIND_GUST_SPEED("gust"),
    MINIMUM_PRECIPITATION_INTENSITY("pmin"),
    MAXIMUM_PRECIPITATION_INTENSITY("pmax"),
    PERCENT_PRECIPITATION_FROZEN_FORM("spp"),
    PRECIPITATION_CATEGORY("pcat"),
    MEAN_PRECIPITATION_INTENSITY("pmean"),
    MEDIAN_PRECIPITATION_INTENSITY("pmedian"),
    WEATHER_SYMBOL("Wsymb2");

    companion object {
        fun getFromString(name: String?): SMHIParameterType? = entries.find { it.value == name }
    }
}

@Serializable
enum class SMHIParameterSymbolType(
    val code: Int,
    val meaning: String
) {
    CLEAR_SKY(1, "Clear sky"),
    NEARLY_CLEAR_SKY(2, "Nearly clear sky"),
    VARIABLE_CLOUDINESS(3, "Variable cloudiness"),
    HALF_CLEAR_SKY(4, "Halfclear sky"),
    CLOUDY_SKY(5, "Cloudy sky"),
    OVERCAST(6, "Overcast"),
    FOG(7, "Fog"),
    LIGHT_RAIN_SHOWERS(8, "Light rain showers"),
    MODERATE_RAIN_SHOWERS(9, "Moderate rain showers"),
    HEAVY_RAIN_SHOWERS(10, "Heavy rain showers"),
    THUNDERSTORM(11, "Thunderstorm"),
    LIGHT_SLEET_SHOWERS(12, "Light sleet showers"),
    MODERATE_SLEET_SHOWERS(13, "Moderate sleet showers"),
    HEAVY_SLEET_SHOWERS(14, "Heavy sleet showers"),
    LIGHT_SNOW_SHOWERS(15, "Light snow showers"),
    MODERATE_SNOW_SHOWERS(16, "Moderate snow showers"),
    HEAVY_SNOW_SHOWERS(17, "Heavy snow showers"),
    LIGHT_RAIN(18, "Light rain"),
    MODERATE_RAIN(19, "Moderate rain"),
    HEAVY_RAIN(20, "Heavy rain"),
    THUNDER(21, "Thunder"),
    LIGHT_SLEET(22, "Light sleet"),
    MODERATE_SLEET(23, "Moderate sleet"),
    HEAVY_SLEET(24, "Heavy sleet"),
    LIGHT_SNOWFALL(25, "Light snowfall"),
    MODERATE_SNOWFALL(26, "Moderate snowfall"),
    HEAVY_SNOWFALL(27, "Heavy snowfall");

    companion object {
        fun fromCode(code: Int): SMHIParameterSymbolType? =
            SMHIParameterSymbolType.entries.find { it.code == code }
    }
}
