package com.example.smartfinance.ui.navigation

import Screen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartfinance.ui.screens.AddTransactionScreen
import com.example.smartfinance.ui.screens.DevSettingsScreen
import com.example.smartfinance.ui.screens.GoalsScreen
import com.example.smartfinance.ui.screens.HomeScreen
import com.example.smartfinance.ui.screens.SplashScreen
import com.example.smartfinance.ui.screens.StatisticsScreen
import com.example.smartfinance.ui.screens.TransactionsListScreen
import com.example.smartfinance.ui.viewmodel.GoalViewModel
import com.example.smartfinance.ui.viewmodel.HomeViewModel
import com.example.smartfinance.ui.viewmodel.StatisticsViewModel
import com.example.smartfinance.ui.viewmodel.TransactionViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    transactionViewModel: TransactionViewModel,
    statisticsViewModel: StatisticsViewModel,
    goalViewModel: GoalViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel
            )
        }

        composable(route = Screen.AddTransaction.route) {
            AddTransactionScreen(
                navController = navController,
                viewModel = transactionViewModel
            )
        }

        composable(route = Screen.Statistics.route) {
            StatisticsScreen(
                navController = navController,
                viewModel = statisticsViewModel
            )
        }

        composable(route = Screen.Goals.route) {
            GoalsScreen(
                navController = navController,
                viewModel = goalViewModel
            )
        }

        composable(route = Screen.Transactions.route) {
            TransactionsListScreen(
                navController = navController,
                viewModel = transactionViewModel
            )
        }

        composable(route = Screen.DevSettings.route) {
            DevSettingsScreen(
                navController = navController,
                transactionViewModel = transactionViewModel,
                goalViewModel = goalViewModel
            )
        }
    }
}