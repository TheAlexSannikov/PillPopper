package sannikov.a.stonerstopwatch
// https://youtu.be/gg-KBGH9T8s
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavGraph(navController: NavHostController, stateViewModel: StateViewModel, sharedPreferences: SharedPreferences, context: Context) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Stopwatch.route
    ) {
        composable(route = BottomBarScreen.Stopwatch.route) {
            StopwatchScreen(stateViewModel = stateViewModel, sharedPreferences = sharedPreferences, context = context)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen("placeholder")
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen("placeholder")
        }
    }
}