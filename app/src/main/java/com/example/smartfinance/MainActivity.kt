package com.example.smartfinance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.smartfinance.ui.navigation.NavGraph
import com.example.smartfinance.ui.theme.SmartFinanceTheme
import com.example.smartfinance.ui.viewmodel.HomeViewModel
import com.example.smartfinance.ui.viewmodel.StatisticsViewModel
import com.example.smartfinance.ui.viewmodel.TransactionViewModel

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels {
        val app = application as SmartFinanceApplication
        ViewModelFactory(
            app.transactionRepository,
            app.categoryRepository,
            app.goalRepository
        )
    }

    private val transactionViewModel: TransactionViewModel by viewModels {
        val app = application as SmartFinanceApplication
        ViewModelFactory(
            app.transactionRepository,
            app.categoryRepository,
            app.goalRepository
        )
    }

    private val statisticsViewModel: StatisticsViewModel by viewModels {
        val app = application as SmartFinanceApplication
        ViewModelFactory(
            app.transactionRepository,
            app.categoryRepository,
            app.goalRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmartFinanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavGraph(
                        navController = navController,
                        homeViewModel = homeViewModel,
                        transactionViewModel = transactionViewModel,
                        statisticsViewModel = statisticsViewModel
                    )
                }
            }
        }
    }
}