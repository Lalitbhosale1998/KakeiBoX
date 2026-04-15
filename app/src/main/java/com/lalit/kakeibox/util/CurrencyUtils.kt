package com.personal.kakeibox.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    // Format with custom symbol and optional privacy mask
    fun formatAmount(amount: Long, symbol: String = "¥", isPrivacyMode: Boolean = false): String {
        if (isPrivacyMode) return "$symbol ••••"
        val formatter = NumberFormat.getNumberInstance(Locale.JAPAN)
        return "$symbol${formatter.format(amount)}"
    }

    // Format as Japanese Yen — ¥1,234,567
    fun formatYen(amount: Long, isPrivacyMode: Boolean = false): String {
        return formatAmount(amount, "¥", isPrivacyMode)
    }

    // Safe parse — returns 0 if invalid input
    fun parseAmount(input: String): Long {
        return input.replace(",", "")
            .replace("¥", "")
            .trim()
            .toLongOrNull() ?: 0L
    }
}