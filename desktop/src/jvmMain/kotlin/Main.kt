import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mes.inc.aic.common.App
import mes.inc.aic.common.data.di.initKoin

fun main() = application {
    initKoin()
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
