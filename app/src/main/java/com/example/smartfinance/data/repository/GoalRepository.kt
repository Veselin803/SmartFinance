package com.example.smartfinance.data.repository

import com.example.smartfinance.data.local.GoalDao
import com.example.smartfinance.data.model.Goal
import kotlinx.coroutines.flow.Flow

/**
 * Repository za ciljeve štednje
 */
class GoalRepository(private val goalDao: GoalDao) {

    // Svi ciljevi
    val allGoals: Flow<List<Goal>> = goalDao.getAllGoals()

    // Aktivni ciljevi
    val activeGoals: Flow<List<Goal>> = goalDao.getActiveGoals()

    // Dodaj cilj
    suspend fun insertGoal(goal: Goal) {
        goalDao.insertGoal(goal)
    }

    // Ažuriraj cilj
    suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal)
    }

    // Obriši cilj
    suspend fun deleteGoal(goal: Goal) {
        goalDao.deleteGoal(goal)
    }
}