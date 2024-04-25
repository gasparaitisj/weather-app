package com.justas.weather.app.main.forecast

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.justas.weather.app.main.theme.AppTypography

@Composable
fun ForecastProviderView(
    provider: String?,
    errorMessage: String?,
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
    if (!errorMessage.isNullOrBlank()) {
        Text(
            modifier =
                Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(0.75f)
                    .then(modifier),
            text = "An error occurred while fetching forecast.",
            style = AppTypography.titleSmall.copy(color = Color.Red),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier =
                Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .height(36.dp)
                    .horizontalScroll(rememberScrollState())
                    .then(modifier),
            text = errorMessage,
            style = AppTypography.labelSmall.copy(color = Color.Red),
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
    }
}
