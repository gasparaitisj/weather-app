package com.justas.weather.app.info

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.justas.weather.app.home.HomeLazyRow

@Composable
fun InfoScreen(
    state: InfoState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeLazyRow(
        modifier =
            Modifier
                .then(modifier),
        items = state.averageForecastItems,
    )
    LaunchedEffect(Unit) {
        onRefresh()
    }
}
