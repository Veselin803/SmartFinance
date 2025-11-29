package com.example.smartfinance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity klasa za finansijske transakcije
 * Svaka transakcija predstavlja jedan prihod ili rashod
 */
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val amount: Double,              // Iznos (pozitivan za prihod, može biti i za rashod)
    val description: String,         // Opis transakcije (npr. "Kupovina u Maxiju")
    val categoryId: Int,             // ID kategorije (foreign key)
    val type: String,                // "income" ili "expense"
    val date: Long,                  // Timestamp (milisekunde)
    val note: String? = null         // Opciona napomena
)