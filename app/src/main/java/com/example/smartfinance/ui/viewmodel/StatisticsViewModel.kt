package com.example.smartfinance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfinance.data.model.Category
import com.example.smartfinance.data.model.Transaction
import com.example.smartfinance.data.repository.CategoryRepository
import com.example.smartfinance.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import java.util.*

/**
 * ViewModel za Statistics ekran
 * Obrada podataka za grafikone i statistiku
 */
class StatisticsViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // Sve transakcije
    val allTransactions: StateFlow<List<Transaction>> =
        transactionRepository.allTransactions
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Sve kategorije
    val allCategories: StateFlow<List<Category>> =
        categoryRepository.allCategories
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    /**
     * Grupisanje transakcija po kategorijama (za pie chart)
     */
    val expensesByCategory: StateFlow<Map<String, Double>> =
        combine(allTransactions, allCategories) { transactions, categories ->
            transactions
                .filter { it.type == "expense" }
                .groupBy { it.categoryId }
                .mapKeys { entry ->
                    categories.find { it.id == entry.key }?.name ?: "Nepoznato"
                }
                .mapValues { entry ->
                    entry.value.sumOf { it.amount }
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    /**
     * Transakcije po mesecima (za bar chart)
     */
    val monthlyExpenses: StateFlow<Map<String, Double>> =
        allTransactions.map { transactions ->
            transactions
                .filter { it.type == "expense" }
                .groupBy {
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = it.date
                    "${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}"
                }
                .mapValues { entry ->
                    entry.value.sumOf { it.amount }
                }
                .toSortedMap()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
}