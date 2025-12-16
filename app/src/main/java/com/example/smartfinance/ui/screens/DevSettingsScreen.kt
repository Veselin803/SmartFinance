package com.example.smartfinance.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
 * Dev Settings ekran - za brzo dodavanje i brisanje test podataka
 * Proširena verzija sa kompletnim kontrolama
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevSettingsScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    goalViewModel: GoalViewModel
) {
    val scope = rememberCoroutineScope()
    var showSuccessMessage by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Collect data for statistics
    val allTransactions by transactionViewModel.allTransactions.collectAsState()
    val allGoals by goalViewModel.allGoals.collectAsState()

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
            // Statistika trenutnih podataka
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📊 Trenutno Stanje",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Divider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Transakcije:")
                        Text(
                            text = "${allTransactions.size}",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ciljevi:")
                        Text(
                            text = "${allGoals.size}",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Text(
                text = "Test Podaci",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

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
                        showSuccessMessage = "✅ Dodato 18 transakcija"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ Dodaj Sample Transakcije (18 kom)")
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
                            // Ažuriraj sa trenutnim iznosom
                            if (goal.currentAmount > 0) {
                                kotlinx.coroutines.delay(100) // Mali delay da se cilj kreira
                                val goals = allGoals
                                val createdGoal = goals.lastOrNull { it.name == goal.name }
                                createdGoal?.let {
                                    goalViewModel.updateGoalProgress(it, goal.currentAmount)
                                }
                            }
                        }
                        showSuccessMessage = "✅ Dodato 4 cilja"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ Dodaj Sample Ciljeve (4 kom)")
            }

            Divider()

            Text(
                text = "Brisanje Podataka",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            // Delete All Button
            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                enabled = allTransactions.isNotEmpty() || allGoals.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("🗑 Obriši SVE Podatke")
            }

            if (allTransactions.isEmpty() && allGoals.isEmpty()) {
                Text(
                    text = "Nema podataka za brisanje",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Divider()

            // Info Card
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
                                "• Možeš ih obrisati pojedinačno ili sve odjednom\n" +
                                "• Korisno za screenshot-ove u dokumentaciji\n" +
                                "• Prikazuje raznolike kategorije i scenarije\n" +
                                "• CRUD operacije dostupne na svim ekranima\n" +
                                "• Swipe udesno na transakciji za brisanje",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Success message
            showSuccessMessage?.let { message ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { showSuccessMessage = null }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }

    // Delete All Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    "⚠ Obriši SVE Podatke?",
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Ova akcija će TRAJNO obrisati:",
                        fontWeight = FontWeight.Bold
                    )

                    Divider()

                    if (allTransactions.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("• Sve transakcije")
                            Text(
                                "${allTransactions.size}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    if (allGoals.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("• Sve ciljeve")
                            Text(
                                "${allGoals.size}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Divider()

                    Text(
                        "Kategorije ostaju (default podaci).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "⚠ Ovo se NE MOŽE poništiti!",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            // Obriši sve transakcije
                            allTransactions.forEach { transaction ->
                                transactionViewModel.deleteTransaction(transaction)
                            }
                            // Obriši sve ciljeve
                            allGoals.forEach { goal ->
                                goalViewModel.deleteGoal(goal)
                            }
                            showDeleteDialog = false
                            showSuccessMessage = "🗑 Svi podaci uspešno obrisani"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("DA, Obriši SVE")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Otkaži")
                }
            }
        )
    }
}