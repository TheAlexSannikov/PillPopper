package sannikov.a.stonerstopwatch.ui.theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors(
    primary: Color,
    secondary: Color,
    textPrimary: Color,
    error: Color,
    isLight: Boolean
) {
    var primary by mutableStateOf(primary)
        private set

    var secondary by mutableStateOf(secondary)
        private set

    var textPrimary by mutableStateOf(textPrimary)
        private set

    var error by mutableStateOf(error)
        private set

    var isLight by mutableStateOf(isLight)
        internal set

    fun copy(
        primary: Color = this.primary,
//        secondary: Color = this.secondary,
        textPrimary: Color = this.textPrimary,
        error: Color = this.error,
        isLight: Boolean = this.isLight
    ): AppColors = AppColors(
        primary,
        secondary,
        textPrimary,
        error,
        isLight,
    )

    // will be explained later
    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        textPrimary = other.textPrimary
//        textSecondary = other.textSecondary
//        background = other.background
        error = other.error
    }
}

// will trigger the whole content lambda to be recomposed once some value is changed.


private val colorWhite = Color(0xFFFFFFFF)
private val colorBlack = Color(0xFF000000)


private val colorLightPrimary = Color(0xFFFFB400)
private val colorLightSecondary = Teal200
private val colorLightTextPrimary = colorBlack
private val colorLightTextSecondary = Color(0xFF6C727A)
private val colorLightBackground = colorWhite
private val colorLightError = Color(0xFFD62222)

private val colorDarkPrimary = Green500
private val colorDarkSecondary = Purple500
private val colorDarkTextPrimary = colorWhite
private val colorDarkTextSecondary = Color(0xFF6C727A)
private val colorDarkBackground = colorBlack
private val colorDarkError = Color(0xFFD62222)

fun lightColors(
    primary: Color = colorLightPrimary,
    secondary: Color = colorLightSecondary,
    textPrimary: Color = colorLightTextPrimary,
    textSecondary: Color = colorLightTextSecondary,
    background: Color = colorLightBackground,
    error: Color = colorLightError
): AppColors = AppColors(
    primary = primary,
    secondary = secondary,
    textPrimary = textPrimary,
//        textSecondary = textSecondary,
//        background = background,
    error = error,
    isLight = true,
)
// TODO: just import 'androidx.compose.material.darkColors'?
fun darkColors(
    primary: Color = colorDarkPrimary,
    secondary: Color = colorDarkSecondary,
    textPrimary: Color = colorDarkTextPrimary,
    textSecondary: Color = colorDarkTextSecondary,
    background: Color = colorDarkBackground,
    error: Color = colorDarkError
): AppColors = AppColors(
    primary = primary,
    secondary = secondary,
    textPrimary = textPrimary,
//        textSecondary = textSecondary,
//        background = background,
    error = error,
    isLight = false,
)

internal val LocalColors = staticCompositionLocalOf{ lightColors() } // TODO: make compositionLocalOf since colors will change?
