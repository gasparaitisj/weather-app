package com.justas.weather.core.domain.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant

data class CommonForecast(
    val items: PersistentList<CommonForecastItem> = persistentListOf(),
    val place: String = "",
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
    }
}
