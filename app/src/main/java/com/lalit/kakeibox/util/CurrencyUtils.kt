package com.personal.kakeibox.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    // Format with custom symbol and optional privacy mask
    fun formatAmount(amount: Long, symbol: String = "¥", isPrivacyMode: Boolean = false): String {
        if (isPrivacyMode) return "$symbol ••••"
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        return "$symbol${formatter.format(amount)}"
    }

    // Safe parse — returns 0 if invalid input
    fun parseAmount(input: String, symbol: String = "¥"): Long {
        return input.replace(",", "")
            .replace(symbol, "")
            .trim()
            .toLongOrNull() ?: 0L
    }
}