package com.example.smartfinance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartfinance.data.model.Goal
import com.example.smartfinance.ui.components.BottomNavBar
import com.example.smartfinance.ui.viewmodel.GoalViewModel

/**
 * Goals ekran - praćenje i dodavanje ciljeva štednje
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    navController: NavController,
    viewModel: GoalViewModel
) {
    val goals by viewModel.allGoals.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ciljevi Štednje",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = { BottomNavBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Dodaj cilj",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        if (goals.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "🎯", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Nema postavljenih ciljeva",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Dodajte prvi cilj klikom na + dugme",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(goals) { goal ->
                    GoalCard(
                        goal = goal,
                        onDelete = { viewModel.deleteGoal(goal) },
                        onUpdateProgress = { newAmount ->
                            viewModel.updateGoalProgress(goal, newAmount)
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddGoalDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, targetAmount, icon ->
                viewModel.addGoal(
                    name = name,
                    targetAmount = targetAmount,
                    icon = icon
                )
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCard(
    goal: Goal,
    onDelete: () -> Unit,
    onUpdateProgress: (Double) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    val progress = if (goal.targetAmount > 0) {
        (goal.currentAmount / goal.targetAmount).coerceIn(0.0, 1.0).toFloat()
    } else {
        0f
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { showEditDialog = true }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = goal.icon, fontSize = 32.sp)
                    Text(
                        text = goal.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Obriši",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatCurrency(goal.currentAmount),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = formatCurrency(goal.targetAmount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                            .fillMaxWidth(fraction = progress)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }

                Text(
                    text = "${(progress * 100).toInt()}% ostvareno",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            goal.deadline?.let { deadline ->
                Text(
                    text = "Rok: ${formatDate(deadline)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }

    if (showEditDialog) {
        EditGoalProgressDialog(
            goal = goal,
            onDismiss = { showEditDialog = false },
            onConfirm = { newAmount ->
                onUpdateProgress(newAmount)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("🎯") }

    val icons = listOf("🎯", "💰", "🏠", "🚗", "✈", "🎓", "💍", "📱")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novi Cilj Štednje") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Naziv cilja") },
                    placeholder = { Text("npr. Letovanje 2024") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = targetAmount,
                    onValueChange = { targetAmount = it },
                    label = { Text("Ciljani iznos (RSD)") },
                    placeholder = { Text("50000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Izaberi ikonu:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    icons.forEach { icon ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (selectedIcon == icon)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surface
                                )
                                .clickable { selectedIcon = icon }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = icon,
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = targetAmount.toDoubleOrNull()
                    if (name.isNotBlank() && amount != null && amount > 0) {
                        onConfirm(name, amount, selectedIcon)
                    }
                },
                enabled = name.isNotBlank() && targetAmount.toDoubleOrNull() != null
            ) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Otkaži")
            }
        }
    )
}

@Composable
fun EditGoalProgressDialog(
    goal: Goal,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var newAmount by remember { mutableStateOf(goal.currentAmount.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ažuriraj Napredak") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${goal.icon} ${goal.name}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = newAmount,
                    onValueChange = { newAmount = it },
                    label = { Text("Trenutno uštеđeno (RSD)") },
                    placeholder = { Text("0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Cilj: ${formatCurrency(goal.targetAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = newAmount.toDoubleOrNull()
                    if (amount != null && amount >= 0) {
                        onConfirm(amount)
                    }
                },
                enabled = newAmount.toDoubleOrNull() != null
            ) {
                Text("Sačuvaj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Otkaži")
            }
        }
    )
}