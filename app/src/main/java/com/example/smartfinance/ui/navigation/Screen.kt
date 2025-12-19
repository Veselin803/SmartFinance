package com.example.smartfinance.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")  // ← DODAJ
    object Register : Screen("register")  // ← DODAJ
    object Home : Screen("home")
    object AddTransaction : Screen("add_transaction")
    object EditTransaction : Screen("edit_transaction/{transactionId}") {
        fun createRoute(transactionId: Int) = "edit_transaction/$transactionId"
    }
    object Transactions : Screen("transactions")
    object Statistics : Screen("statistics")
    object Goals : Screen("goals")
    object Settings : Screen("settings")
    object DevSettings : Screen("dev_settings")
}