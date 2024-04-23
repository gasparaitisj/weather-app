package com.justas.weather.app.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.justas.weather.app.main.theme.AppTypography
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.model.CommonForecastItem
import com.justas.weather.core.domain.model.CommonWindDirection
import com.justas.weather.core.domain.repository.ForecastState
import com.justas.weather.core.util.double.round
import com.justas.weather.core.util.double.toScaleString
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun HomeScreen(
    state: ForecastState,
    modifier: Modifier = Modifier
) {
    HomeContent(
        modifier =
            Modifier
                .padding(
                    start = 12.dp,
                    top = 12.dp,
                )
                .then(modifier),
        state = state,
    )
}

@Composable
private fun HomeContent(
    state: ForecastState,
    modifier: Modifier = Modifier,
) {
    HomeLazyColumn(
        modifier =
            Modifier
                .then(modifier),
        forecasts = state.forecasts,
    )
}

@Composable
private fun HomeLazyColumn(
    forecasts: ImmutableList<CommonForecast>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(forecasts.size) { index ->
            val forecast = forecasts[index]
            Column(
                modifier =
                    Modifier
                        .padding(
                            bottom = 24.dp,
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ProviderView(
                    provider = forecast.provider,
                )
                HomeLazyRow(
                    items = forecast.items,
                )
                HomeLoadingView(
                    isLoading = forecast.isLoading,
                )
                HomeErrorView(
                    errorMessage = forecast.errorMessage,
                )
            }
        }
    }
}

@Composable
private fun HomeErrorView(
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    if (errorMessage.isBlank()) return
    Text(
        modifier =
            Modifier
                .padding(top = 12.dp)
                .then(modifier),
        text = errorMessage,
    )
}

@Composable
private fun HomeLoadingView(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!isLoading) return
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
                .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun HomeLazyRow(
    items: ImmutableList<CommonForecastItem>,
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
            )
        }
    }
}

@Composable
private fun ForecastItem(
    item: CommonForecastItem?,
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
            direction = item.windDirection,
            gust = item.windGust,
            speed = item.windSpeed,
        )
    }
}

@Composable
private fun WindView(
    direction: Double?,
    gust: Double?,
    speed: Double?,
) {
    if (direction == null && gust == null && speed == null) return
    Text(
        text =
            buildString {
                append("Wind is")
                if (direction != null) {
                    append(" ${CommonWindDirection.getDirection(direction.roundToInt())}")
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
private fun ProviderView(
    provider: String?,
    modifier: Modifier = Modifier,
) {
    if (provider.isNullOrBlank()) return
    Text(
        modifier =
            Modifier
                .fillMaxWidth(0.75f)
                .then(modifier),
        text = provider,
        style = AppTypography.titleLarge,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
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
private fun TimeView(instant: Instant?) {
    val dateTime =
        instant?.toLocalDateTime(
            TimeZone.currentSystemDefault(),
        ) ?: return
    Text(
        text = "${dateTime.date} ${dateTime.time}",
        color = MaterialTheme.colorScheme.primary,
    )
}
