package com.justas.weather.core.domain.model

data class CommonDailyForecastItem(
    val averageWindDirection: Double? = null,
    val averageWindGust: Double? = null,
    val averageWindSpeed: Double? = null,
    val highAirTemperature: Double? = null,
    val highCloudCover: Double? = null,
    val highFeelsLikeTemperature: Double? = null,
    val highRelativeHumidity: Double? = null,
    val highSeaLevelPressure: Double? = null,
    val highTotalPrecipitation: Double? = null,
    val lowAirTemperature: Double? = null,
    val lowCloudCover: Double? = null,
    val lowFeelsLikeTemperature: Double? = null,
    val lowRelativeHumidity: Double? = null,
    val lowSeaLevelPressure: Double? = null,
    val lowTotalPrecipitation: Double? = null,
)
