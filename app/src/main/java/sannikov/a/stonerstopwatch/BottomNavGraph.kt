package sannikov.a.stonerstopwatch
// https://youtu.be/gg-KBGH9T8s
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen("placeholder")
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen("placeholder")
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen("placeholder")
        }
    }
}