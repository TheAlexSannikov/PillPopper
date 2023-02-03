package sannikov.a.stonerstopwatch
// https://youtu.be/gg-KBGH9T8s
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import sannikov.a.stonerstopwatch.pilltimer.PillTimerScreen
import sannikov.a.stonerstopwatch.views.StopwatchScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState
) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Stopwatch.route
    ) {
        composable(route = BottomBarScreen.Stopwatch.route) {
            StopwatchScreen()
        }
        composable(route = BottomBarScreen.PillTimer.route) {
            PillTimerScreen(scaffoldState = scaffoldState)
        }
    }
}