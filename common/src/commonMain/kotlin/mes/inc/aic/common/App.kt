package mes.inc.aic.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import mes.inc.aic.common.ui.ArtSpaceTheme
import mes.inc.aic.common.ui.HomeScreen

@Composable
fun App() {
    ArtSpaceTheme {
        HomeScreen(modifier = Modifier.fillMaxSize())
    }
}
