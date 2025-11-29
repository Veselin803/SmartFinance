package com.example.smartfinance.data.repository

import com.example.smartfinance.data.local.CategoryDao
import com.example.smartfinance.data.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Repository za kategorije
 */
class CategoryRepository(private val categoryDao: CategoryDao) {

    // Sve kategorije
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    // Kategorije po tipu
    fun getCategoriesByType(type: String): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type)
    }

    // Dobavi kategoriju po ID
    suspend fun getCategoryById(id: Int): Category? {
        return categoryDao.getCategoryById(id)
    }

    // Dodaj kategoriju
    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }
}