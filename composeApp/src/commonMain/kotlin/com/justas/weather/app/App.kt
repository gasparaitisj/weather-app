package com.justas.weather.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.justas.weather.core.datasource.network.LTMLoader
import com.justas.weather.core.util.string.prettyPrint
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.compose_multiplatform

private val log =
    Logger(
        loggerConfigInit(platformLogWriter()),
        "App",
    )
private val ltmLoader =
    LTMLoader(
        httpClient = ServiceLocator.httpClient,
    )

@Composable
@Preview
fun App() {
    AppContent()
    LaunchedEffect(Unit) {
        val ltmForecast = ltmLoader.getForecast()
        log.d { ltmForecast.prettyPrint() }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun AppContent() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { showContent = !showContent },
            ) {
                Text("Click me!")
            }
            AnimatedVisibility(
                visible = showContent,
            ) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(Res.drawable.compose_multiplatform),
                        contentDescription = null,
                    )
                    Text(
                        text = "Compose: $greeting",
                    )
                }
            }
        }
    }
}
