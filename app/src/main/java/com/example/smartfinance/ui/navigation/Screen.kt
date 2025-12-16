sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object AddTransaction : Screen("add_transaction")
    object Transactions : Screen("transactions")
    object Statistics : Screen("statistics")
    object Goals : Screen("goals")
    object Settings : Screen("settings")
    object DevSettings : Screen("dev_settings")
}