package com.justas.weather.app.info

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.justas.weather.core.di.ServiceLocator.log
import com.justas.weather.core.util.string.prettyPrint

@Composable
fun InfoScreen(
    state: InfoState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(state.averageForecastItems) {
        log.d { state.averageForecastItems.prettyPrint() }
    }
}
