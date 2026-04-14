package com.personal.kakeibox.data.preferences

enum class CurrencyPreference(val symbol: String, val label: String) {
    JPY("¥", "JPY (¥)"),
    INR("₹", "INR (₹)"),
    USD("$", "USD ($)");

    companion object {
        fun fromStorage(name: String?): CurrencyPreference {
            return entries.find { it.name == name } ?: JPY
        }
    }
}
