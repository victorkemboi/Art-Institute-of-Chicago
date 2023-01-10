package mes.inc.aic.common.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

val Primary = Color(0xFFB50938)
val PrimaryDark = Color(0xFF7E0F2E)
val PrimaryLight = Color(0xFFE18371)
val SpaceGray = Color(0xFF2E3538)

private val darkColorPalette = darkColors(
    primary = Primary,
    primaryVariant = PrimaryDark,
    secondary = PrimaryLight,
    onPrimary = White,
    background = SpaceGray
)

private val lightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryDark,
    secondary = PrimaryLight,
    background = White,
    onPrimary = SpaceGray,
)

object Padding {
    /**
     *  4 dp
     */
    val Small = 4.dp

    /**
     *  8 dp
     */
    val Medium = 8.dp

    /**
     *  16 dp
     */
    val Normal = 16.dp

    /**
     *  32 dp
     */
    val Large = 32.dp
}

@Composable
fun ArtSpaceTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }
    MaterialTheme(
        colors = colors,
        content = {
            ProvideTextStyle(
                value = TextStyle(color = MaterialTheme.colors.onPrimary),
                content = { Surface(color = MaterialTheme.colors.background, content = content) }
            )
        }
    )

}