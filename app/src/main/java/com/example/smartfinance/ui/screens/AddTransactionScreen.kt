package com.example.smartfinance.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartfinance.data.model.Category
import com.example.smartfinance.ui.viewmodel.TransactionViewModel

/**
 * Ekran za dodavanje nove transakcije
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel
) {
    // State
    var selectedType by remember { mutableStateOf("expense") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    // Collect categories from ViewModel
    val categories by viewModel.allCategories.collectAsState()
    val filteredCategories = categories.filter { it.type == selectedType }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Transakcija") },
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
            // Type Selector (Income/Expense)
            TypeSelector(
                selectedType = selectedType,
                onTypeSelected = {
                    selectedType = it
                    selectedCategory = null
                }
            )

            // Amount Input
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Iznos (RSD)") },
                placeholder = { Text("0") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Description Input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Opis") },
                placeholder = { Text("Npr. Kupovina u prodavnici") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Category Selector
            OutlinedButton(
                onClick = { showCategoryDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedCategory?.let { "${it.icon} ${it.name}" }
                        ?: "Izaberi kategoriju",
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (amountValue != null && amountValue > 0 &&
                        description.isNotBlank() && selectedCategory != null) {

                        viewModel.addTransaction(
                            amount = amountValue,
                            description = description,
                            categoryId = selectedCategory!!.id,
                            type = selectedType
                        )

                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = amount.toDoubleOrNull() != null &&
                        description.isNotBlank() &&
                        selectedCategory != null
            ) {
                Text(
                    text = "Sačuvaj Transakciju",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    // Category Selection Dialog
    if (showCategoryDialog) {
        CategorySelectionDialog(
            categories = filteredCategories,
            onCategorySelected = {
                selectedCategory = it
                showCategoryDialog = false
            },
            onDismiss = { showCategoryDialog = false }
        )
    }
}

/**
 * Selector za tip transakcije (Prihod/Rashod)
 */
@Composable
fun TypeSelector(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TypeButton(
            text = "Rashod",
            icon = "💸",
            isSelected = selectedType == "expense",
            onClick = { onTypeSelected("expense") },
            modifier = Modifier.weight(1f)
        )

        TypeButton(
            text = "Prihod",
            icon = "💰",
            isSelected = selectedType == "income",
            onClick = { onTypeSelected("income") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TypeButton(
    text: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

/**
 * Dialog za izbor kategorije
 */
@Composable
fun CategorySelectionDialog(
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Izaberi Kategoriju") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategorySelected(category) },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.icon,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Otkaži")
            }
        }
    )
}