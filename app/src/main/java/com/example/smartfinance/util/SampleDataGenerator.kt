package com.example.smartfinance.util

import com.example.smartfinance.data.model.Goal
import com.example.smartfinance.data.model.Transaction
import java.util.*

/**
 * Generator sample podataka za testiranje i prikaz u dokumentaciji
 * Ovo je "hard-coded" pristup koji si želeo za diplomski
 */
object SampleDataGenerator {

    /**
     * Generiše sample transakcije za poslednjih 30 dana
     */
    fun generateSampleTransactions(): List<Transaction> {
        val calendar = Calendar.getInstance()
        val transactions = mutableListOf<Transaction>()

        // Rashodi - razne kategorije
        val expenses = listOf(
            Triple("Kupovina u Maxiju", 3500.0, 1),      // Hrana
            Triple("Gorivo", 4000.0, 2),                  // Transport
            Triple("Bioskop", 1200.0, 3),                 // Zabava
            Triple("Račun za struju", 5500.0, 4),         // Računi
            Triple("Kupovina odeće", 8000.0, 5),          // Kupovina
            Triple("Apoteka", 2500.0, 6),                 // Zdravlje
            Triple("Knjige", 3000.0, 7),                  // Obrazovanje
            Triple("Kafić", 800.0, 3),                    // Zabava
            Triple("Pijaca", 2000.0, 1),                  // Hrana
            Triple("Taxi", 1500.0, 2),                    // Transport
            Triple("Netflix pretplata", 1200.0, 3),       // Zabava
            Triple("Restoran", 4500.0, 1),                // Hrana
            Triple("Benzin", 5000.0, 2),                  // Transport
            Triple("Šoping", 12000.0, 5),                 // Kupovina
            Triple("Lekovi", 1800.0, 6),                  // Zdravlje
        )

        // Generiši rashode kroz poslednjih 30 dana
        expenses.forEachIndexed { index, (desc, amount, catId) ->
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.DAY_OF_MONTH, -(index * 2)) // Svaki drugi dan

            transactions.add(
                Transaction(
                    id = index + 1,
                    amount = amount,
                    description = desc,
                    categoryId = catId,
                    type = "expense",
                    date = calendar.timeInMillis
                )
            )
        }

        // Prihodi
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        transactions.add(
            Transaction(
                id = 100,
                amount = 60000.0,
                description = "Plata za decembar",
                categoryId = 9, // Plata
                type = "income",
                date = calendar.timeInMillis
            )
        )

        calendar.add(Calendar.DAY_OF_MONTH, -15)
        transactions.add(
            Transaction(
                id = 101,
                amount = 15000.0,
                description = "Honorar za projekat",
                categoryId = 11, // Honorar
                type = "income",
                date = calendar.timeInMillis
            )
        )

        calendar.add(Calendar.DAY_OF_MONTH, -10)
        transactions.add(
            Transaction(
                id = 102,
                amount = 5000.0,
                description = "Poklon od roditelja",
                categoryId = 10, // Poklon
                type = "income",
                date = calendar.timeInMillis
            )
        )

        return transactions
    }

    /**
     * Generiše sample ciljeve
     */
    fun generateSampleGoals(): List<Goal> {
        val calendar = Calendar.getInstance()

        return listOf(
            Goal(
                id = 1,
                name = "Letovanje 2024",
                targetAmount = 100000.0,
                currentAmount = 35000.0,
                deadline = calendar.apply {
                    add(Calendar.MONTH, 6)
                }.timeInMillis,
                icon = "✈"
            ),
            Goal(
                id = 2,
                name = "Novi laptop",
                targetAmount = 150000.0,
                currentAmount = 80000.0,
                deadline = calendar.apply {
                    timeInMillis = System.currentTimeMillis()
                    add(Calendar.MONTH, 3)
                }.timeInMillis,
                icon = "💻"
            ),
            Goal(
                id = 3,
                name = "Fond za hitne slučajeve",
                targetAmount = 50000.0,
                currentAmount = 20000.0,
                deadline = null,
                icon = "🏦"
            ),
            Goal(
                id = 4,
                name = "Novi telefon",
                targetAmount = 80000.0,
                currentAmount = 15000.0,
                deadline = calendar.apply {
                    timeInMillis = System.currentTimeMillis()
                    add(Calendar.MONTH, 4)
                }.timeInMillis,
                icon = "📱"
            )
        )
    }
}