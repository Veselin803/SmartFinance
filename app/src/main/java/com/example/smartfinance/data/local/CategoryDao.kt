package com.example.smartfinance.data.local

import androidx.room.*
import com.example.smartfinance.data.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * DAO za rad sa kategorijama
 */
@Dao
interface CategoryDao {

    // Dobavi sve kategorije (automatski se ažurira kad se baza promeni)
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    // Dobavi kategorije po tipu (expense ili income)
    @Query("SELECT * FROM categories WHERE type = :type")
    fun getCategoriesByType(type: String): Flow<List<Category>>

    // Dobavi kategoriju po ID-u
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?

    // Dodaj novu kategoriju
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    // Dodaj više kategorija odjednom
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    // Obriši kategoriju
    @Delete
    suspend fun deleteCategory(category: Category)
}