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
import com.justas.weather.core.di.ServiceLocator.weatherRepository

@Composable
fun MainScaffold(modifier: Modifier = Modifier) {
    val state by weatherRepository.state.collectAsState()
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .then(modifier),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MainTopBar(
                onRefresh = weatherRepository::onRefresh,
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
