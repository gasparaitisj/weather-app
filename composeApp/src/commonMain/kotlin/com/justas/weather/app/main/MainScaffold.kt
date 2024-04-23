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
import com.justas.weather.app.info.InfoScreen
import com.justas.weather.app.info.InfoViewModel
import com.justas.weather.app.main.bottombar.BottomBarItem
import com.justas.weather.app.main.bottombar.BottomBarViewModel
import com.justas.weather.app.main.bottombar.MainBottomBar
import com.justas.weather.app.main.topbar.MainTopBar
import com.justas.weather.app.main.topbar.TopBarViewModel
import com.justas.weather.core.domain.repository.ForecastRepository

@Composable
fun MainScaffold(
    forecastRepository: ForecastRepository,
    topBarViewModel: TopBarViewModel,
    bottomBarViewModel: BottomBarViewModel,
    infoViewModel: InfoViewModel,
    modifier: Modifier = Modifier,
) {
    val forecastState by forecastRepository.state.collectAsState()
    val topBarState by topBarViewModel.state.collectAsState()
    val bottomBarState by bottomBarViewModel.state.collectAsState()
    val infoState by infoViewModel.state.collectAsState()
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .then(modifier),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (bottomBarState.selectedItem == BottomBarItem.HOME) {
                MainTopBar(
                    state = topBarState,
                    onItemSelected = topBarViewModel::onPlaceSelected,
                    onRefresh = {
                        forecastRepository.onRefresh(topBarState.selectedPlace)
                    },
                )
            }
        },
        bottomBar = {
            MainBottomBar(
                items = bottomBarState.items,
                selectedItem = bottomBarState.selectedItem,
                onItemClick = bottomBarViewModel::onItemSelected,
            )
        },
    ) { paddingValues ->
        when (bottomBarState.selectedItem) {
            BottomBarItem.HOME -> {
                HomeScreen(
                    modifier =
                        Modifier
                            .padding(paddingValues),
                    state = forecastState,
                )
            }
            BottomBarItem.INFO -> {
                InfoScreen(
                    modifier =
                        Modifier
                            .padding(paddingValues),
                    state = infoState,
                    onRefresh = infoViewModel::onRefresh,
                )
            }
        }
    }
}
