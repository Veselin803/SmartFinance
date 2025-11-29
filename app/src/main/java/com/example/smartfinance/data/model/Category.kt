package com.example.smartfinance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity klasa za kategorije transakcija
 * Npr: Hrana, Transport, Zabava, Plaćanja računa, itd.
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,           // Naziv kategorije (npr. "Hrana")
    val icon: String,           // Emoji ili ime ikone (npr. "🍔")
    val color: String,          // Hex boja (npr. "#FF5722")
    val type: String            // "expense" ili "income"
)