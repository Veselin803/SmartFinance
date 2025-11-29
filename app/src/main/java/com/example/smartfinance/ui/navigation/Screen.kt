package com.example.smartfinance.ui.navigation

/**
 * Sealed class za definisanje svih ekrana u aplikaciji
 * Koristi se za type-safe navigaciju
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTransaction : Screen("add_transaction")
    object Transactions : Screen("transactions")
    object Statistics : Screen("statistics")
    object Goals : Screen("goals")
    object Settings : Screen("settings")
}