package com.justas.weather.core.data.response
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.WindDirection
import kotlin.math.roundToInt
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
data class OWMForecastResponse(
    @SerialName("city")
    val city: City?,
    @SerialName("cnt")
    val cnt: Double?,
    @SerialName("cod")
    val cod: String?,
    @SerialName("list")
    val list: List<OWMItem?>?,
    @SerialName("message")
    val message: Double?
) {
    fun toModel(provider: String): CommonForecast {
        return CommonForecast(
            items =
                list?.map { item ->
                    CommonForecastItem(
                        airTemperature = item?.main?.temp,
                        cloudCover = item?.clouds?.all,
                        conditionCode = item?.weather?.firstOrNull()?.description.orEmpty(),
                        instant = item?.dtTxt.toInstantOrNull(),
                        feelsLikeTemperature = item?.main?.feelsLike,
                        relativeHumidity = item?.main?.humidity,
                        seaLevelPressure = item?.main?.pressure,
                        totalPrecipitation = item?.pop,
                        windDirection =
                            WindDirection.getDirection(
                                item?.wind?.deg?.roundToInt(),
                            ),
                        windGust = item?.wind?.gust,
                        windSpeed = item?.wind?.speed,
                    )
                }.orEmpty().toPersistentList(),
            place = city?.name.orEmpty(),
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
data class City(
    @SerialName("coord")
    val coord: Coord?,
    @SerialName("country")
    val country: String?,
    @SerialName("id")
    val id: Double?,
    @SerialName("name")
    val name: String?,
    @SerialName("population")
    val population: Double?,
    @SerialName("sunrise")
    val sunrise: Double?,
    @SerialName("sunset")
    val sunset: Double?,
    @SerialName("timezone")
    val timezone: Double?
)

@Serializable
data class OWMItem(
    @SerialName("clouds")
    val clouds: Clouds?,
    @SerialName("dt")
    val dt: Double?,
    @SerialName("dt_txt")
    val dtTxt: String?,
    @SerialName("main")
    val main: Main?,
    @SerialName("pop")
    val pop: Double?,
    @SerialName("rain")
    val rain: Rain?,
    @SerialName("snow")
    val snow: Snow?,
    @SerialName("sys")
    val sys: Sys?,
    @SerialName("visibility")
    val visibility: Double?,
    @SerialName("weather")
    val weather: List<Weather?>?,
    @SerialName("wind")
    val wind: Wind?
)

@Serializable
data class Coord(
    @SerialName("lat")
    val lat: Double?,
    @SerialName("lon")
    val lon: Double?
)

@Serializable
data class Clouds(
    @SerialName("all")
    val all: Double?
)

@Serializable
data class Main(
    @SerialName("feels_like")
    val feelsLike: Double?,
    @SerialName("grnd_level")
    val grndLevel: Double?,
    @SerialName("humidity")
    val humidity: Double?,
    @SerialName("pressure")
    val pressure: Double?,
    @SerialName("sea_level")
    val seaLevel: Double?,
    @SerialName("temp")
    val temp: Double?,
    @SerialName("temp_kf")
    val tempKf: Double?,
    @SerialName("temp_max")
    val tempMax: Double?,
    @SerialName("temp_min")
    val tempMin: Double?
)

@Serializable
data class Rain(
    @SerialName("3h")
    val h: Double?
)

@Serializable
data class Snow(
    @SerialName("3h")
    val h: Double?
)

@Serializable
data class Sys(
    @SerialName("pod")
    val pod: String?
)

@Serializable
data class Weather(
    @SerialName("description")
    val description: String?,
    @SerialName("icon")
    val icon: String?,
    @SerialName("id")
    val id: Double?,
    @SerialName("main")
    val main: String?
)

@Serializable
data class Wind(
    @SerialName("deg")
    val deg: Double?,
    @SerialName("gust")
    val gust: Double?,
    @SerialName("speed")
    val speed: Double?
)
