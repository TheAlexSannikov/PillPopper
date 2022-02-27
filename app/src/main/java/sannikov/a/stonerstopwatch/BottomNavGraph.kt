package sannikov.a.stonerstopwatch
// https://youtu.be/gg-KBGH9T8s
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    stateViewModel: StateViewModel,
    dataStoreManager: DataStoreManager
) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Stopwatch.route
    ) {
        composable(route = BottomBarScreen.Stopwatch.route) {
            StopwatchScreen(stateViewModel = stateViewModel, dataStoreManager = dataStoreManager)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen("placeholder")
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen("placeholder")
        }
    }
}