package com.justas.weather.app.main.forecast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.CommonWindDirection
import com.justas.weather.core.util.double.round
import com.justas.weather.core.util.double.toScaleString
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ForecastLazyRow(
    items: ImmutableList<CommonForecastItem>,
    isDateShown: Boolean = true,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return
    LazyRow(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 12.dp)
                .then(modifier),
    ) {
        items(items.size) { index ->
            ForecastItem(
                item = items[index],
                isDateShown = isDateShown,
            )
        }
    }
}

@Composable
private fun ForecastItem(
    item: CommonForecastItem?,
    isDateShown: Boolean = true,
    modifier: Modifier = Modifier,
) {
    if (item == null) return
    Column(
        modifier =
            Modifier
                .padding(end = 24.dp)
                .then(modifier),
    ) {
        TimeView(
            instant = item.instant,
            isDateShown = isDateShown,
        )
        TemperatureView(
            airTemperature = item.airTemperature,
            feelsLikeTemperature = item.feelsLikeTemperature,
        )
        CloudView(
            cloudCover = item.cloudCover,
        )
        DescriptionView(
            description = item.condition,
        )
        HumidityView(
            relativeHumidity = item.relativeHumidity,
        )
        PressureView(
            seaLevelPressure = item.seaLevelPressure,
        )
        PrecipitationView(
            totalPrecipitation = item.totalPrecipitation,
        )
        WindView(
            direction = CommonWindDirection.getDirection(item.windDirection?.roundToInt())?.value,
            gust = item.windGust,
            speed = item.windSpeed,
        )
    }
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

@Composable
private fun PrecipitationView(totalPrecipitation: Double?) {
    if (totalPrecipitation == null) return
    Text(
        text = "Total precipitation is ${totalPrecipitation.toScaleString(2)} mm",
    )
}

@Composable
private fun PressureView(seaLevelPressure: Double?) {
    if (seaLevelPressure == null) return
    Text(
        text = "Sea level pressure is ${seaLevelPressure.toScaleString(1)} hPa",
    )
}

@Composable
private fun HumidityView(relativeHumidity: Double?) {
    if (relativeHumidity == null) return
    Text(
        text = "Relative humidity is ${relativeHumidity.toScaleString(2)}%",
    )
}

@Composable
private fun DescriptionView(description: String?) {
    if (description.isNullOrBlank()) return
    Text(
        text = "Weather condition is $description",
    )
}

@Composable
private fun CloudView(cloudCover: Double?) {
    if (cloudCover == null) return
    Text(
        text = "Cloud cover is ${cloudCover.toScaleString(2)}%",
    )
}

@Composable
private fun TemperatureView(
    airTemperature: Double?,
    feelsLikeTemperature: Double?,
) {
    if (airTemperature != null) {
        Text(
            text = "Air temperature is ${airTemperature.round(1)} °C.",
        )
    }
    if (feelsLikeTemperature != null) {
        Text(
            text = "Feels like ${feelsLikeTemperature.round(1)} °C.",
        )
    }
}

@Composable
private fun TimeView(
    instant: Instant?,
    isDateShown: Boolean = true,
) {
    val dateTime =
        instant?.toLocalDateTime(
            TimeZone.currentSystemDefault(),
        ) ?: return
    Text(
        text =
            buildString {
                if (isDateShown) {
                    append("${dateTime.date} ")
                }
                append("${dateTime.time}")
            },
        color = MaterialTheme.colorScheme.primary,
    )
}
