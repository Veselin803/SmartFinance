package com.example.smartfinance.data.local

import androidx.room.*
import com.example.smartfinance.data.model.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * DAO za rad sa transakcijama
 */
@Dao
interface TransactionDao {

    // Sve transakcije sortirane po datumu (najnovije prvo)
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    // Transakcije po tipu
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Flow<List<Transaction>>

    // Transakcije iz određenog perioda
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>

    // Transakcije po kategoriji
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getTransactionsByCategory(categoryId: Int): Flow<List<Transaction>>

    // Ukupan iznos po tipu (za statistiku)
    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    fun getTotalByType(type: String): Flow<Double?>

    // Dodaj transakciju
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    // Obriši transakciju
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    // Ažuriraj transakciju
    @Update
    suspend fun updateTransaction(transaction: Transaction)
}