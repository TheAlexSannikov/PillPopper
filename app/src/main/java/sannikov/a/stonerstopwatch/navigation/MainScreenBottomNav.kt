package sannikov.a.stonerstopwatch.navigation
// https://youtu.be/gg-KBGH9T8s

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import sannikov.a.stonerstopwatch.ui.theme.StonerStopwatchTheme


@Composable
fun MainScreenBottomNav() {
    StonerStopwatchTheme {
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()

        // Create a coroutine scope. Opening of Drawer and snackbar should happen in background thread without blocking main thread
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            bottomBar = { BottomBar(navController = navController) },
            scaffoldState = scaffoldState,
        ) { paddingValues ->
            BottomNavGraph(
                navController = navController,
                scaffoldState = scaffoldState,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Stopwatch,
        BottomBarScreen.PillTimer,
        BottomBarScreen.Settings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        for (screen in screens) {
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = { navController.navigate(screen.route) },
        alwaysShowLabel = false,
    )
}
