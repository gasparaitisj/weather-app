package com.justas.weather.app.info.topbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.justas.weather.app.main.theme.AppTypography
import com.justas.weather.core.domain.model.CommonPlace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTopBarView(
    place: CommonPlace,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier =
            Modifier
                .fillMaxWidth()
                .then(modifier),
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        title = { InfoTopBarTitle(place) },
    )
}

@Composable
private fun InfoTopBarTitle(place: CommonPlace) {
    Text(
        modifier =
            Modifier
                .padding(start = 16.dp),
        text =
            buildString {
                append(place.name)
                if (place.administrativeDivision.isNotBlank()) {
                    append(", ${place.administrativeDivision}")
                }
                if (place.countryName.isNotBlank()) {
                    append(", ${place.countryName}")
                }
                if (place.countryCode.isNotBlank()) {
                    append(", ${place.countryCode}")
                }
            },
        style = AppTypography.titleLarge,
        overflow = TextOverflow.Ellipsis,
    )
}
