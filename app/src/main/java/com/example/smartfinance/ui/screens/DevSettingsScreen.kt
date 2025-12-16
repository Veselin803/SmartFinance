package com.example.smartfinance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartfinance.ui.viewmodel.GoalViewModel
import com.example.smartfinance.ui.viewmodel.TransactionViewModel
import com.example.smartfinance.util.SampleDataGenerator
import kotlinx.coroutines.launch

/**
 * Dev Settings ekran - za brzo dodavanje test podataka
 * Koristi se samo za development/demonstraciju
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevSettingsScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    goalViewModel: GoalViewModel
) {
    val scope = rememberCoroutineScope()
    var showSuccessMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Developer Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Nazad"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Test Podaci za Prezentaciju",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ove opcije dodaju hard-coded test podatke za lakšu demonstraciju aplikacije u dokumentaciji.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Divider()

            // Add Sample Transactions Button
            Button(
                onClick = {
                    scope.launch {
                        SampleDataGenerator.generateSampleTransactions().forEach { transaction ->
                            transactionViewModel.addTransaction(
                                amount = transaction.amount,
                                description = transaction.description,
                                categoryId = transaction.categoryId,
                                type = transaction.type,
                                date = transaction.date
                            )
                        }
                        showSuccessMessage = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dodaj Sample Transakcije (15 kom)")
            }

            // Add Sample Goals Button
            Button(
                onClick = {
                    scope.launch {
                        SampleDataGenerator.generateSampleGoals().forEach { goal ->
                            goalViewModel.addGoal(
                                name = goal.name,
                                targetAmount = goal.targetAmount,
                                deadline = goal.deadline,
                                icon = goal.icon
                            )
                        }
                        showSuccessMessage = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dodaj Sample Ciljeve (4 kom)")
            }

            Divider()

            // Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "ℹ Napomena:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "• Sample podaci se dodaju u realnu bazu\n" +
                                "• Možeš ih obrisati ručno\n" +
                                "• Korisno za screenshot-ove u dokumentaciji\n" +
                                "• Prikazuje raznolike kategorije i scenarije",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (showSuccessMessage) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "✅ Podaci uspešno dodati!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}