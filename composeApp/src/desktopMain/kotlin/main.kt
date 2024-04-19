import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.justas.weather.app.App

fun main() =
    application {
        Window(onCloseRequest = ::exitApplication, title = "Weather") {
            App()
        }
    }
