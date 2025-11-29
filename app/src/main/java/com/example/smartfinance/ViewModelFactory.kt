package com.example.smartfinance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartfinance.data.repository.CategoryRepository
import com.example.smartfinance.data.repository.GoalRepository
import com.example.smartfinance.data.repository.TransactionRepository
import com.example.smartfinance.ui.viewmodel.HomeViewModel
import com.example.smartfinance.ui.viewmodel.TransactionViewModel

/**
 * Factory za kreiranje ViewModel-a sa dependency injection
 */
class ViewModelFactory(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val goalRepository: GoalRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HomeViewModel(transactionRepository, categoryRepository, goalRepository) as T
            }
            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                TransactionViewModel(transactionRepository, categoryRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}