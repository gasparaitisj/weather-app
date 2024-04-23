package com.justas.weather.app.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.justas.weather.app.home.HomeScreen
import com.justas.weather.core.domain.repository.ForecastRepository

@Composable
fun MainScaffold(
    forecastRepository: ForecastRepository,
    mainTopBarViewModel: MainTopBarViewModel,
    modifier: Modifier = Modifier,
) {
    val state by forecastRepository.state.collectAsState()
    val mainTopBarState by mainTopBarViewModel.state.collectAsState()
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .then(modifier),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MainTopBar(
                state = mainTopBarState,
                onItemSelected = mainTopBarViewModel::onPlaceSelected,
                onExpandedChange = mainTopBarViewModel::onDropdownMenuExpandedChange,
                onTextFieldValueChange = mainTopBarViewModel::onDropdownMenuTextFieldValueChange,
                onRefresh = forecastRepository::onRefresh,
            )
        },
    ) { paddingValues ->
        HomeScreen(
            modifier =
                Modifier
                    .padding(paddingValues),
            state = state,
        )
    }
}
