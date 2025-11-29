package com.example.smartfinance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfinance.data.model.Category
import com.example.smartfinance.data.model.Transaction
import com.example.smartfinance.data.repository.CategoryRepository
import com.example.smartfinance.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel za rad sa transakcijama
 * Dodavanje, brisanje, ažuriranje transakcija
 */
class TransactionViewModel(
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

    // Sve kategorije (za dropdown pri dodavanju transakcije)
    val allCategories: StateFlow<List<Category>> =
        categoryRepository.allCategories
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Dodaj novu transakciju
    fun addTransaction(
        amount: Double,
        description: String,
        categoryId: Int,
        type: String,
        date: Long = System.currentTimeMillis(),
        note: String? = null
    ) {
        viewModelScope.launch {
            val transaction = Transaction(
                amount = amount,
                description = description,
                categoryId = categoryId,
                type = type,
                date = date,
                note = note
            )
            transactionRepository.insertTransaction(transaction)
        }
    }

    // Obriši transakciju
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.deleteTransaction(transaction)
        }
    }

    // Ažuriraj transakciju
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.updateTransaction(transaction)
        }
    }
}