package sannikov.a.stonerstopwatch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

// https://proandroiddev.com/how-to-create-a-truly-custom-theme-in-jetpack-compose-55fb4cd6d655
private val DarkColorPalette = darkColors(
    primary = Green500,
    secondary = Purple500,
)

private val LightColorPalette = lightColors(
    primary = Green200Dark,
    secondary = Purple200Dark,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

//    val typography: AppTypography
//        @Composable
//        @ReadOnlyComposable
//        get() = LocalTypography.current

    val dimensions: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current
}

//@Composable
//fun StonerStopwatchTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    content: @Composable() () -> Unit
//) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
//
//    MaterialTheme(
//        colors = colors,
//        typography = Typography,
//        shapes = Shapes,
//        content = content
//    )
//}

@Composable
fun AppTheme(
    colors: AppColors? = AppTheme.colors,
//    typography: AppTypography = AppTheme.typography,
    dimensions: AppDimensions = AppTheme.dimensions,
    content: @Composable () -> Unit
) {
    val darkTheme: Boolean = isSystemInDarkTheme()

    val newColors = colors ?: if (darkTheme) darkColors() else lightColors()

    // creating a new object for colors to not mutate the initial colors set when updating the values
    val rememberedColors = remember { newColors.copy() }.apply { updateColorsFrom(newColors) }

    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalDimensions provides dimensions,
//        LocalTypography provides typography
    ) {
        content()
    }
}