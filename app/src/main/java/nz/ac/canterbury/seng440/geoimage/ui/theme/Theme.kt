package nz.ac.canterbury.seng440.geoimage.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Green_Dark,
    primaryVariant = Yellowish,
    secondary = Brown_Dark,
    secondaryVariant = Brown_Dark,
    error = Red_Dark,

    background = Black,
    surface = Black,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = White,
    onSurface = White,
    onError = Black
)

private val LightColorPalette = lightColors(
    primary = Green_Light,
    primaryVariant = Yellowish,
    secondary = Brown_Light,
    secondaryVariant = Brown_Dark,
    error= Red_Light,

    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
    onError = White
)

@Composable
fun GeoImageTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}