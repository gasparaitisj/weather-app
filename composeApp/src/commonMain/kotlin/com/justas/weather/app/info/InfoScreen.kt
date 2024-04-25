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
import com.justas.weather.core.domain.model.CommonForecastItem
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
        forecastItemsByHour = infoState.averageForecastItemsByHour,
        forecastItemsByDay = infoState.averageForecastItemsByDay,
        isLoading = infoState.isLoading,
    )
}

@Composable
private fun InfoContent(
    forecastItemsByHour: ImmutableList<CommonForecastItem>,
    forecastItemsByDay: ImmutableList<Pair<LocalDate, ImmutableList<CommonForecastItem>>>,
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
            forecastItemsByHour = forecastItemsByHour,
            forecastItemsByDay = forecastItemsByDay,
        )
    }
}

@Composable
private fun InfoLazyColumn(
    forecastItemsByHour: ImmutableList<CommonForecastItem>,
    forecastItemsByDay: ImmutableList<Pair<LocalDate, ImmutableList<CommonForecastItem>>>,
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
                val (dateTime, items) = forecastItemsByDay[index]
                TimeView(
                    dateTime = dateTime,
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
private fun TimeView(dateTime: LocalDate) {
    Text(
        text = dateTime.toString(),
        style = AppTypography.titleLarge.copy(MaterialTheme.colorScheme.primary),
    )
}
