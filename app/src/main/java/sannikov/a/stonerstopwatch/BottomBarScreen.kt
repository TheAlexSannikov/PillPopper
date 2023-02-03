package sannikov.a.stonerstopwatch
// https://youtu.be/gg-KBGH9T8s
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Stopwatch: BottomBarScreen(
        route = "stopwatch",
        title = "Stopwatch",
        icon = Icons.Default.Home
    )
    object PillTimer: BottomBarScreen(
        route = "pillTimer",
        title = "Pill Timer",
        icon = Icons.Default.AddCircle
    )

    object Settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}
