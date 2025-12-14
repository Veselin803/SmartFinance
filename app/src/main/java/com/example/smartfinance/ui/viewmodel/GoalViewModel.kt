package com.example.smartfinance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfinance.data.model.Goal
import com.example.smartfinance.data.repository.GoalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel za upravljanje ciljevima štednje
 */
class GoalViewModel(
    private val goalRepository: GoalRepository
) : ViewModel() {

    // Svi ciljevi
    val allGoals: StateFlow<List<Goal>> =
        goalRepository.allGoals
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Aktivni ciljevi (još nisu postignuti)
    val activeGoals: StateFlow<List<Goal>> =
        goalRepository.activeGoals
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Dodaj novi cilj
    fun addGoal(
        name: String,
        targetAmount: Double,
        deadline: Long? = null,
        icon: String = "🎯"
    ) {
        viewModelScope.launch {
            val goal = Goal(
                name = name,
                targetAmount = targetAmount,
                currentAmount = 0.0,
                deadline = deadline,
                icon = icon
            )
            goalRepository.insertGoal(goal)
        }
    }

    // Ažuriraj napredak cilja
    fun updateGoalProgress(goal: Goal, newAmount: Double) {
        viewModelScope.launch {
            val updatedGoal = goal.copy(currentAmount = newAmount)
            goalRepository.updateGoal(updatedGoal)
        }
    }

    // Obriši cilj
    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            goalRepository.deleteGoal(goal)
        }
    }
}