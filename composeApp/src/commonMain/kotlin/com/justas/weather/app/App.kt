package com.justas.weather.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.justas.weather.app.home.topbar.HomeTopBarViewModel
import com.justas.weather.app.info.InfoViewModel
import com.justas.weather.app.main.MainScaffold
import com.justas.weather.app.main.bottombar.BottomBarViewModel
import com.justas.weather.app.main.theme.AppTheme
import com.justas.weather.core.di.ServiceLocator
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        val forecastRepository = ServiceLocator.forecastRepository
        val homeTopBarViewModel =
            remember {
                HomeTopBarViewModel(
                    ServiceLocator.ltmPlacesApi,
                    forecastRepository,
                )
            }
        val bottomBarViewModel = remember { BottomBarViewModel() }
        val infoViewModel = remember { InfoViewModel(forecastRepository) }
        MainScaffold(
            forecastRepository = forecastRepository,
            homeTopBarViewModel = homeTopBarViewModel,
            bottomBarViewModel = bottomBarViewModel,
            infoViewModel = infoViewModel,
        )
    }
}
