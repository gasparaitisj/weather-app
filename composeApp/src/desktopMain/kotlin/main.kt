import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.justas.weather.app.App

fun main() =
    application {
        DesktopApp()
    }

@Composable
private fun ApplicationScope.DesktopApp() {
    Window(onCloseRequest = ::exitApplication, title = "Weather") {
        App()
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
