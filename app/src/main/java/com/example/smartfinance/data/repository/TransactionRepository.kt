package com.example.smartfinance.data.repository

import com.example.smartfinance.data.local.TransactionDao
import com.example.smartfinance.data.model.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Repository za transakcije
 * Enkapsulira logiku pristupa podacima
 */
class TransactionRepository(private val transactionDao: TransactionDao) {

    // Sve transakcije kao Flow (reaktivno)
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    // Dobavi transakcije po tipu
    fun getTransactionsByType(type: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(type)
    }

    // Dobavi transakcije iz perioda
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
    }

    // Ukupan iznos po tipu
    fun getTotalByType(type: String): Flow<Double?> {
        return transactionDao.getTotalByType(type)
    }

    // Dodaj transakciju
    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    // Obriši transakciju
    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    // Ažuriraj transakciju
    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }
}