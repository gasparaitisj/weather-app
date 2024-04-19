package com.justas.weather.core.entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OWMForecast(
    @SerialName("city")
    val city: City?,
    @SerialName("cnt")
    val cnt: Int?,
    @SerialName("cod")
    val cod: String?,
    @SerialName("list")
    val list: List<OWMList?>?,
    @SerialName("message")
    val message: Int?
)

@Serializable
internal data class City(
    @SerialName("coord")
    val coord: Coord?,
    @SerialName("country")
    val country: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("population")
    val population: Int?,
    @SerialName("sunrise")
    val sunrise: Int?,
    @SerialName("sunset")
    val sunset: Int?,
    @SerialName("timezone")
    val timezone: Int?
)

@Serializable
internal data class OWMList(
    @SerialName("clouds")
    val clouds: Clouds?,
    @SerialName("dt")
    val dt: Int?,
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
    val visibility: Int?,
    @SerialName("weather")
    val weather: List<Weather?>?,
    @SerialName("wind")
    val wind: Wind?
)

@Serializable
internal data class Coord(
    @SerialName("lat")
    val lat: Double?,
    @SerialName("lon")
    val lon: Double?
)

@Serializable
internal data class Clouds(
    @SerialName("all")
    val all: Int?
)

@Serializable
internal data class Main(
    @SerialName("feels_like")
    val feelsLike: Double?,
    @SerialName("grnd_level")
    val grndLevel: Int?,
    @SerialName("humidity")
    val humidity: Int?,
    @SerialName("pressure")
    val pressure: Int?,
    @SerialName("sea_level")
    val seaLevel: Int?,
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
internal data class Rain(
    @SerialName("3h")
    val h: Double?
)

@Serializable
internal data class Snow(
    @SerialName("3h")
    val h: Double?
)

@Serializable
internal data class Sys(
    @SerialName("pod")
    val pod: String?
)

@Serializable
internal data class Weather(
    @SerialName("description")
    val description: String?,
    @SerialName("icon")
    val icon: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("main")
    val main: String?
)

@Serializable
internal data class Wind(
    @SerialName("deg")
    val deg: Int?,
    @SerialName("gust")
    val gust: Double?,
    @SerialName("speed")
    val speed: Double?
)
