package com.personal.kakeibox.data.preferences

data class ThemeSettings(
    val darkThemePreference: DarkThemePreference = DarkThemePreference.SYSTEM,
    val useDynamicColor: Boolean = true
)
