package com.example.smartfinance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfinance.data.model.Category
import com.example.smartfinance.data.model.Goal
import com.example.smartfinance.data.model.Transaction
import com.example.smartfinance.data.repository.CategoryRepository
import com.example.smartfinance.data.repository.GoalRepository
import com.example.smartfinance.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel za Home/Dashboard ekran
 * Upravlja stanjem i logikom glavnog ekrana
 */
class HomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val goalRepository: GoalRepository
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

    // Aktivni ciljevi
    val activeGoals: StateFlow<List<Goal>> =
        goalRepository.activeGoals
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Ukupan prihod
    val totalIncome: StateFlow<Double> =
        transactionRepository.getTotalByType("income")
            .map { it ?: 0.0 }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0.0
            )

    // Ukupan rashod
    val totalExpense: StateFlow<Double> =
        transactionRepository.getTotalByType("expense")
            .map { it ?: 0.0 }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0.0
            )

    // Balans (prihod - rashod)
    val balance: StateFlow<Double> = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )
}