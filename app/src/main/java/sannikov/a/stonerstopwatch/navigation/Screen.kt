package sannikov.a.stonerstopwatch.navigation

// sealed class: a class that only allows classes within it to inherited from it, similar to enum
sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object SettingsScreen: Screen("settings_screen")

    /**
     * creates a route string, appending each arg to the string
     * NOTE: Assumes all args are required!
     */
    fun withArgs(vararg args: String): String {
        return buildString{
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
