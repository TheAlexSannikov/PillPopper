package sannikov.a.stonerstopwatch.navigation
// https://youtu.be/gg-KBGH9T8s
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import sannikov.a.stonerstopwatch.pilltimer.PillTimerScreen
import sannikov.a.stonerstopwatch.views.SettingsScreen
import sannikov.a.stonerstopwatch.views.StopwatchScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Stopwatch.route
    ) {
        composable(route = BottomBarScreen.Stopwatch.route) {
            StopwatchScreen(modifier = modifier)
        }
        composable(route = BottomBarScreen.PillTimer.route) {
            PillTimerScreen(scaffoldState = scaffoldState, modifier = modifier)
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen("placeholder", modifier = modifier)
        }
    }
}