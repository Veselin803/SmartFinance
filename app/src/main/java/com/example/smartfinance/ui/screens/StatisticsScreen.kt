package com.example.smartfinance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartfinance.ui.components.BottomNavBar
import com.example.smartfinance.ui.theme.CategoryBlue
import com.example.smartfinance.ui.theme.CategoryCyan
import com.example.smartfinance.ui.theme.CategoryOrange
import com.example.smartfinance.ui.theme.CategoryPink
import com.example.smartfinance.ui.theme.CategoryPurple
import com.example.smartfinance.ui.theme.CategoryYellow
import com.example.smartfinance.ui.viewmodel.StatisticsViewModel

/**
 * Statistics ekran - prikazuje grafikone i analize
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: StatisticsViewModel
) {
    val expensesByCategory by viewModel.expensesByCategory.collectAsState()
    val monthlyExpenses by viewModel.monthlyExpenses.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Statistika",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = "Rashodi po Kategorijama",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (expensesByCategory.isEmpty()) {
                item {
                    EmptyStatisticsCard()
                }
            } else {
                item {
                    CategoryExpensesCard(expensesByCategory)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Mesečni Trendovi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (monthlyExpenses.isEmpty()) {
                item {
                    EmptyStatisticsCard()
                }
            } else {
                item {
                    MonthlyTrendsCard(monthlyExpenses)
                }
            }
        }
    }
}

@Composable
fun CategoryExpensesCard(expenses: Map<String, Double>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val maxExpense = expenses.values.maxOrNull() ?: 1.0
            val colors = listOf(
                CategoryOrange, CategoryBlue, CategoryPurple,
                CategoryYellow, CategoryPink, CategoryCyan
            )

            expenses.entries.forEachIndexed { index, entry ->
                CategoryBar(
                    categoryName = entry.key,
                    amount = entry.value,
                    maxAmount = maxExpense,
                    color = colors[index % colors.size]
                )
            }
        }
    }
}

@Composable
fun CategoryBar(
    categoryName: String,
    amount: Double,
    maxAmount: Double,
    color: Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formatCurrency(amount),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = (amount / maxAmount).toFloat())
                    .clip(RoundedCornerShape(6.dp))
                    .background(color)
            )
        }
    }
}

@Composable
fun MonthlyTrendsCard(monthlyData: Map<String, Double>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val maxValue = monthlyData.values.maxOrNull() ?: 1.0

            monthlyData.entries.toList().takeLast(6).forEach { entry ->
                MonthlyBar(
                    monthLabel = entry.key,
                    amount = entry.value,
                    maxAmount = maxValue
                )
            }
        }
    }
}

@Composable
fun MonthlyBar(
    monthLabel: String,
    amount: Double,
    maxAmount: Double
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = monthLabel,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = formatCurrency(amount),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = (amount / maxAmount).toFloat())
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun EmptyStatisticsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "📈", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nema podataka",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}