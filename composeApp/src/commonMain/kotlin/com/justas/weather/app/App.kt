package com.justas.weather.app

import androidx.compose.runtime.Composable
import com.justas.weather.app.main.MainScaffold
import com.justas.weather.app.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        MainScaffold()
    }
}
