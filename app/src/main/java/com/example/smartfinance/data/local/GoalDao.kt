package com.example.smartfinance.data.local

import androidx.room.*
import com.example.smartfinance.data.model.Goal
import kotlinx.coroutines.flow.Flow

/**
 * DAO za rad sa ciljevima štednje
 */
@Dao
interface GoalDao {

    // Svi ciljevi
    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<Goal>>

    // Aktivni ciljevi (još nisu dostignuti)
    @Query("SELECT * FROM goals WHERE currentAmount < targetAmount ORDER BY deadline ASC")
    fun getActiveGoals(): Flow<List<Goal>>

    // Dobavi cilj po ID-u
    @Query("SELECT * FROM goals WHERE id = :id")
    suspend fun getGoalById(id: Int): Goal?

    // Dodaj cilj
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    // Ažuriraj cilj
    @Update
    suspend fun updateGoal(goal: Goal)

    // Obriši cilj
    @Delete
    suspend fun deleteGoal(goal: Goal)
}