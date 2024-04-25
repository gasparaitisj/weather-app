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
import com.justas.weather.app.home.topbar.HomeTopBarView
import com.justas.weather.app.home.topbar.HomeTopBarViewModel
import com.justas.weather.app.info.InfoScreen
import com.justas.weather.app.info.InfoViewModel
import com.justas.weather.app.info.topbar.InfoTopBarView
import com.justas.weather.app.main.bottombar.BottomBarItem
import com.justas.weather.app.main.bottombar.BottomBarView
import com.justas.weather.app.main.bottombar.BottomBarViewModel
import com.justas.weather.core.domain.repository.ForecastRepository

@Composable
fun MainScaffold(
    forecastRepository: ForecastRepository,
    homeTopBarViewModel: HomeTopBarViewModel,
    bottomBarViewModel: BottomBarViewModel,
    infoViewModel: InfoViewModel,
    modifier: Modifier = Modifier,
) {
    val forecastState by forecastRepository.state.collectAsState()
    val homeTopBarState by homeTopBarViewModel.state.collectAsState()
    val bottomBarState by bottomBarViewModel.state.collectAsState()
    val infoState by infoViewModel.state.collectAsState()
    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .then(modifier),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            when (bottomBarState.selectedItem) {
                BottomBarItem.HOME -> {
                    HomeTopBarView(
                        state = homeTopBarState,
                        onItemSelected = homeTopBarViewModel::onPlaceSelected,
                        onRefresh = {
                            homeTopBarViewModel.getPlaces()
                            forecastRepository.onRefresh(homeTopBarState.selectedPlace)
                        },
                    )
                }
                BottomBarItem.INFO -> {
                    InfoTopBarView(
                        place = homeTopBarState.selectedPlace,
                    )
                }
            }
        },
        bottomBar = {
            BottomBarView(
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
                    infoState = infoState,
                )
            }
        }
    }
}
