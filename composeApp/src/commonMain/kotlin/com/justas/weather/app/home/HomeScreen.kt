package com.justas.weather.app.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.justas.weather.app.main.forecast.ForecastLazyRow
import com.justas.weather.app.main.forecast.ForecastLoadingView
import com.justas.weather.app.main.forecast.ForecastProviderView
import com.justas.weather.core.domain.model.CommonForecast
import com.justas.weather.core.domain.repository.ForecastState
import kotlinx.collections.immutable.ImmutableList

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
                ForecastProviderView(
                    provider = forecast.provider,
                    errorMessage = forecast.errorMessage,
                )
                ForecastLazyRow(
                    items = forecast.items,
                )
                ForecastLoadingView(
                    isLoading = forecast.isLoading,
                )
            }
        }
    }
}
