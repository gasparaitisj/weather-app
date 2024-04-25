package com.justas.weather.app.main.forecast

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ForecastLoadingView(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!isLoading) return
    Box(
        modifier =
            Modifier
                .padding(top = 12.dp)
                .fillMaxSize()
                .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
