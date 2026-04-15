package com.personal.kakeibox.data.preferences

data class ThemeSettings(
    val darkThemePreference: DarkThemePreference = DarkThemePreference.SYSTEM,
    val useDynamicColor: Boolean = true,
    val navBarStyle: NavBarStyle = NavBarStyle.FULL_WIDTH
)
