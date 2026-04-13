package com.personal.kakeibox.data.preferences

enum class DarkThemePreference {
    SYSTEM,
    LIGHT,
    DARK;

    fun isDark(systemIsDark: Boolean): Boolean = when (this) {
        SYSTEM -> systemIsDark
        LIGHT -> false
        DARK -> true
    }

    companion object {
        fun fromStorage(value: String?): DarkThemePreference =
            entries.find { it.name == value } ?: SYSTEM
    }
}
