package com.justas.weather.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.justas.weather.app.main.MainScaffold
import com.justas.weather.app.main.topbar.MainTopBarViewModel
import com.justas.weather.app.theme.AppTheme
import com.justas.weather.core.di.ServiceLocator
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        val forecastRepository = ServiceLocator.forecastRepository
        val mainTopBarViewModel =
            remember { MainTopBarViewModel(ServiceLocator.ltmPlacesApi) }
        MainScaffold(
            forecastRepository = forecastRepository,
            mainTopBarViewModel = mainTopBarViewModel,
        )
    }
}
