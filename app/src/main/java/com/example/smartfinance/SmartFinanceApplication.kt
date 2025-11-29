package com.example.smartfinance

import android.app.Application
import com.example.smartfinance.data.local.AppDatabase
import com.example.smartfinance.data.repository.CategoryRepository
import com.example.smartfinance.data.repository.GoalRepository
import com.example.smartfinance.data.repository.TransactionRepository

/**
 * Application klasa - entry point aplikacije
 * Ovde inicijalizujemo database i repository-je
 */
class SmartFinanceApplication : Application() {

    // Database instanca - lazy inicijalizacija
    val database by lazy { AppDatabase.getDatabase(this) }

    // Repository instance
    val transactionRepository by lazy { TransactionRepository(database.transactionDao()) }
    val categoryRepository by lazy { CategoryRepository(database.categoryDao()) }
    val goalRepository by lazy { GoalRepository(database.goalDao()) }
}