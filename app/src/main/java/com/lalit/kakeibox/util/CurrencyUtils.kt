package com.personal.kakeibox.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    // Format as Japanese Yen — ¥1,234,567
    fun formatYen(amount: Long): String {
        val formatter = NumberFormat.getNumberInstance(Locale.JAPAN)
        return "¥${formatter.format(amount)}"
    }

    // Format with custom symbol
    fun formatAmount(amount: Long, symbol: String = "¥"): String {
        val formatter = NumberFormat.getNumberInstance(Locale.JAPAN)
        return "$symbol${formatter.format(amount)}"
    }

    // Safe parse — returns 0 if invalid input
    fun parseAmount(input: String): Long {
        return input.replace(",", "")
            .replace("¥", "")
            .trim()
            .toLongOrNull() ?: 0L
    }
}