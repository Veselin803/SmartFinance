package com.example.smartfinance.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartfinance.ui.screens.HomeScreen
import com.example.smartfinance.ui.screens.AddTransactionScreen
import com.example.smartfinance.ui.viewmodel.HomeViewModel
import com.example.smartfinance.ui.viewmodel.TransactionViewModel

/**
 * Glavni navigation graph za aplikaciju
 * Definiše sve rute i koji ekran se prikazuje
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    transactionViewModel: TransactionViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home/Dashboard ekran
        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel
            )
        }

        // Dodavanje transakcije ekran
        composable(route = Screen.AddTransaction.route) {
            AddTransactionScreen(
                navController = navController,
                viewModel = transactionViewModel
            )
        }

        // Ostali ekrani - dodaćemo kasnije
        // composable(route = Screen.Transactions.route) { ... }
        // composable(route = Screen.Statistics.route) { ... }
        // composable(route = Screen.Goals.route) { ... }
    }
}