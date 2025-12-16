package com.example.smartfinance.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Helper funkcije za formatiranje valute i datuma
 */

fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("sr", "RS"))
    formatter.maximumFractionDigits = 0
    return formatter.format(amount).replace("RSD", "din")
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale("sr", "RS"))
    return sdf.format(Date(timestamp))
}