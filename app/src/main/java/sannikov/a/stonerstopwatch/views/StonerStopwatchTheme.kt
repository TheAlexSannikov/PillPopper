package sannikov.a.stonerstopwatch.views

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun StonerStopwatchTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (dark) darkColors() else lightColors()
    MaterialTheme(colors = colorScheme, content = content)
}