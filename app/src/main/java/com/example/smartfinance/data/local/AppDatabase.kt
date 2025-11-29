package com.example.smartfinance.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smartfinance.data.model.Category
import com.example.smartfinance.data.model.Goal
import com.example.smartfinance.data.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Glavna Room Database klasa za SmartFinance aplikaciju
 * Verzija 1 - inicijalna implementacija
 */
@Database(
    entities = [Transaction::class, Category::class, Goal::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Abstract DAO getter metode
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun goalDao(): GoalDao

    companion object {
        // Singleton pattern - samo jedna instanca baze
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Ako instanca već postoji, vrati je
            return INSTANCE ?: synchronized(this) {
                // Kreiraj novu instancu
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smartfinance_database"
                )
                    .addCallback(DatabaseCallback()) // Dodaj callback za inicijalne podatke
                    .build()

                INSTANCE = instance
                instance
            }
        }

        /**
         * Callback koji se poziva kada se baza prvi put kreira
         * Ovde ubacujemo default kategorije
         */
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                // Popuni bazu inicijalnim podacima
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.categoryDao())
                    }
                }
            }
        }

        /**
         * Popunjava bazu sa default kategorijama
         * Ovo će se izvršiti samo prvi put kada se app pokrene
         */
        suspend fun populateDatabase(categoryDao: CategoryDao) {
            // Default kategorije za RASHODE
            val expenseCategories = listOf(
                Category(name = "Hrana", icon = "🍔", color = "#FF5722", type = "expense"),
                Category(name = "Transport", icon = "🚗", color = "#2196F3", type = "expense"),
                Category(name = "Zabava", icon = "🎉", color = "#9C27B0", type = "expense"),
                Category(name = "Računi", icon = "📄", color = "#F44336", type = "expense"),
                Category(name = "Kupovina", icon = "🛍", color = "#E91E63", type = "expense"),
                Category(name = "Zdravlje", icon = "💊", color = "#00BCD4", type = "expense"),
                Category(name = "Obrazovanje", icon = "📚", color = "#3F51B5", type = "expense"),
                Category(name = "Ostalo", icon = "📦", color = "#607D8B", type = "expense")
            )

            // Default kategorije za PRIHODE
            val incomeCategories = listOf(
                Category(name = "Plata", icon = "💰", color = "#4CAF50", type = "income"),
                Category(name = "Poklon", icon = "🎁", color = "#FFEB3B", type = "income"),
                Category(name = "Honorar", icon = "💼", color = "#009688", type = "income"),
                Category(name = "Ostalo", icon = "💵", color = "#8BC34A", type = "income")
            )

            // Ubaci sve kategorije u bazu
            categoryDao.insertCategories(expenseCategories + incomeCategories)
        }
    }
}