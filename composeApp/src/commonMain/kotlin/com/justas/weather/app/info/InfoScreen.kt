package com.justas.weather.app.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.justas.weather.app.main.forecast.ForecastLazyRow
import com.justas.weather.app.main.forecast.ForecastLoadingView
import com.justas.weather.app.main.forecast.ForecastProviderView
import com.justas.weather.app.main.theme.AppTypography
import com.justas.weather.core.domain.model.CommonDailyForecastItem
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.CommonWindDirection
import com.justas.weather.core.util.double.round
import com.justas.weather.core.util.double.toScaleString
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate

@Composable
fun InfoScreen(
    infoState: InfoState,
    modifier: Modifier = Modifier
) {
    InfoContent(
        modifier =
            Modifier.padding(
                start = 12.dp,
                top = 12.dp,
            ).then(modifier),
        forecastItemsByDay = infoState.averageForecastItemsByDay,
        isLoading = infoState.isLoading,
    )
}

@Composable
private fun InfoContent(
    forecastItemsByDay: ImmutableList<
        Triple<
            LocalDate,
            ImmutableList<CommonForecastItem>,
            CommonDailyForecastItem,
        >,
    >,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isLoading) {
        ForecastLoadingView(
            modifier =
                Modifier
                    .then(modifier),
            isLoading = isLoading,
        )
    } else {
        InfoLazyColumn(
            modifier =
                Modifier
                    .then(modifier),
            forecastItemsByDay = forecastItemsByDay,
        )
    }
}

@Composable
private fun InfoLazyColumn(
    forecastItemsByDay: ImmutableList<
        Triple<
            LocalDate,
            ImmutableList<CommonForecastItem>,
            CommonDailyForecastItem,
        >,
    >,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (forecastItemsByDay.isEmpty()) return@item
            ForecastProviderView(
                modifier =
                    Modifier
                        .padding(bottom = 12.dp),
                provider = "Daily average forecasts",
                errorMessage = null,
            )
        }
        items(forecastItemsByDay.size) { index ->
            if (forecastItemsByDay.isEmpty()) return@items
            Column(
                modifier =
                    Modifier
                        .padding(
                            bottom = 24.dp,
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val (dateTime, items, dailyItem) = forecastItemsByDay[index]
                InfoTimeView(
                    dateTime = dateTime,
                )
                DailyForecastItem(
                    modifier =
                        Modifier
                            .align(Alignment.Start),
                    item = dailyItem,
                )
                ForecastLazyRow(
                    modifier = Modifier.padding(top = 12.dp),
                    items = items,
                    isDateShown = false,
                )
            }
        }
    }
}

@Composable
private fun InfoTimeView(dateTime: LocalDate) {
    Text(
        text = dateTime.toString(),
        style = AppTypography.titleLarge.copy(MaterialTheme.colorScheme.primary),
    )
}

@Composable
private fun DailyForecastItem(
    item: CommonDailyForecastItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            Modifier
                .then(modifier),
    ) {
        TemperatureView(
            highAirTemperature = item.highAirTemperature,
            lowAirTemperature = item.lowAirTemperature,
            highFeelsLikeTemperature = item.highFeelsLikeTemperature,
            lowFeelsLikeTemperature = item.lowFeelsLikeTemperature,
        )
        CloudView(
            lowCloudCover = item.lowCloudCover,
            highCloudCover = item.highCloudCover,
        )
        HumidityView(
            lowRelativeHumidity = item.lowRelativeHumidity,
            highRelativeHumidity = item.highRelativeHumidity,
        )
        PressureView(
            lowSeaLevelPressure = item.lowSeaLevelPressure,
            highSeaLevelPressure = item.highSeaLevelPressure,
        )
        PrecipitationView(
            lowTotalPrecipitation = item.lowTotalPrecipitation,
            highTotalPrecipitation = item.highTotalPrecipitation,
        )
        WindView(
            direction =
                CommonWindDirection.getDirection(
                    item.averageWindDirection?.roundToInt(),
                )?.value,
            gust = item.averageWindGust,
            speed = item.averageWindSpeed,
        )
    }
}

@Composable
private fun TemperatureView(
    highAirTemperature: Double?,
    lowAirTemperature: Double?,
    highFeelsLikeTemperature: Double?,
    lowFeelsLikeTemperature: Double?,
) {
    Text(
        text =
            buildString {
                append("Air temperature is ")
                append("${highAirTemperature?.round(1)} °C / ")
                append("${lowAirTemperature?.round(1)} °C")
            },
    )
    Text(
        text =
            buildString {
                append("Feels like ")
                append("${highFeelsLikeTemperature?.round(1)} °C / ")
                append("${lowFeelsLikeTemperature?.round(1)} °C")
            },
    )
}

@Composable
private fun CloudView(
    lowCloudCover: Double?,
    highCloudCover: Double?,
) {
    Text(
        text =
            buildString {
                append("Cloud cover is ")
                append("${highCloudCover.toScaleString(2)} % / ")
                append("${lowCloudCover.toScaleString(2)} %")
            },
    )
}

@Composable
private fun HumidityView(
    highRelativeHumidity: Double?,
    lowRelativeHumidity: Double?,
) {
    Text(
        text =
            buildString {
                append("Relative humidity is ")
                append("${highRelativeHumidity.toScaleString(2)} % / ")
                append("${lowRelativeHumidity.toScaleString(2)} %")
            },
    )
}

@Composable
private fun PressureView(
    highSeaLevelPressure: Double?,
    lowSeaLevelPressure: Double?,
) {
    Text(
        text =
            buildString {
                append("Relative humidity is ")
                append("${highSeaLevelPressure.toScaleString(1)} hPa / ")
                append("${lowSeaLevelPressure.toScaleString(1)} hPa")
            },
    )
}

@Composable
private fun PrecipitationView(
    highTotalPrecipitation: Double?,
    lowTotalPrecipitation: Double?,
) {
    Text(
        text =
            buildString {
                append("Total precipitation is ")
                append("${highTotalPrecipitation.toScaleString(2)} mm / ")
                append("${lowTotalPrecipitation.toScaleString(2)} mm")
            },
    )
}

@Composable
private fun WindView(
    direction: String?,
    gust: Double?,
    speed: Double?,
) {
    if (direction.isNullOrBlank() && gust == null && speed == null) return
    Text(
        text =
            buildString {
                append("Wind is")
                if (!direction.isNullOrBlank()) {
                    append(" $direction")
                }
                if (speed != null) {
                    append(" ${speed.toScaleString(2)} m/s")
                }
                if (gust != null) {
                    if (speed != null || direction != null) {
                        append(",")
                    }
                    append(" gust is ${gust.toScaleString(2)} m/s")
                }
            },
    )
}
