package com.example.smartfinance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity klasa za ciljeve štednje
 * Korisnik može postaviti ciljeve (npr. "Štednja za automobil")
 */
@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,                // Naziv cilja (npr. "Letovanje 2024")
    val targetAmount: Double,        // Ciljani iznos
    val currentAmount: Double = 0.0, // Trenutno uštеđeno
    val deadline: Long? = null,      // Rok (timestamp) - opciono
    val icon: String = "🎯",         // Emoji ikona
    val createdAt: Long = System.currentTimeMillis()
)