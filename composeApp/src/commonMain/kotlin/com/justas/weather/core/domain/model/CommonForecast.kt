package com.justas.weather.core.domain.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

data class CommonForecast(
    val items: PersistentList<CommonForecastItem> = persistentListOf(),
    val provider: String = "",
    val creationDateTime: Instant? = null,
    val isLoading: Boolean = true,
    val errorMessage: String = "",
)

data class CommonForecastItem(
    val airTemperature: Double? = null,
    val cloudCover: Double? = null,
    val condition: String = "",
    val feelsLikeTemperature: Double? = null,
    val instant: Instant? = null,
    val relativeHumidity: Double? = null,
    val seaLevelPressure: Double? = null,
    val totalPrecipitation: Double? = null,
    val windDirection: Double? = null,
    val windGust: Double? = null,
    val windSpeed: Double? = null,
) {
    companion object {
        val WithZeroes =
            CommonForecastItem(
                airTemperature = 0.0,
                cloudCover = 0.0,
                feelsLikeTemperature = 0.0,
                relativeHumidity = 0.0,
                seaLevelPressure = 0.0,
                totalPrecipitation = 0.0,
                windDirection = 0.0,
                windGust = 0.0,
                windSpeed = 0.0,
            )
        val Example1 =
            CommonForecastItem(
                airTemperature = 14.2,
                cloudCover = 41.0,
                condition = "partly-cloudy",
                feelsLikeTemperature = 14.2,
                instant =
                    LocalDateTime(
                        date =
                            LocalDate(
                                year = 9999,
                                monthNumber = 1,
                                dayOfMonth = 1,
                            ),
                        time = LocalTime(hour = 1, minute = 1),
                    ).toInstant(TimeZone.UTC),
                relativeHumidity = 39.0,
                seaLevelPressure = 1008.0,
                totalPrecipitation = 0.0,
                windDirection = 124.0,
                windGust = 8.0,
                windSpeed = 3.0,
            )
        val Example2 =
            CommonForecastItem(
                airTemperature = 24.2,
                cloudCover = 48.0,
                condition = "partly-cloudy",
                feelsLikeTemperature = 24.2,
                instant =
                    LocalDateTime(
                        date =
                            LocalDate(
                                year = 9999,
                                monthNumber = 1,
                                dayOfMonth = 1,
                            ),
                        time = LocalTime(hour = 2, minute = 1),
                    ).toInstant(TimeZone.UTC),
                relativeHumidity = 56.0,
                seaLevelPressure = 1013.0,
                totalPrecipitation = 0.2,
                windDirection = 250.0,
                windGust = 9.0,
                windSpeed = 4.0,
            )
    }
}
