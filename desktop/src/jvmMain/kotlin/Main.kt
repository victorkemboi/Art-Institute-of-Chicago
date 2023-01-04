import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mes.inc.aic.common.App


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
