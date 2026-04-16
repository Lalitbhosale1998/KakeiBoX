package com.personal.kakeibox.data.preferences

enum class AppLanguage {
    ENGLISH, JAPANESE
}

data class ThemeSettings(
    val darkThemePreference: DarkThemePreference = DarkThemePreference.SYSTEM,
    val useDynamicColor: Boolean = true,
    val navBarStyle: NavBarStyle = NavBarStyle.FULL_WIDTH,
    val remindersEnabled: Boolean = false,
    val currencySymbol: String = "¥",
    val dateFormat: String = "MMM dd, yyyy",
    val appLanguage: AppLanguage = AppLanguage.ENGLISH,
    val biometricEnabled: Boolean = false,
    val tabOrder: List<String> = listOf("salary", "spend", "commute", "settings"),
    val privacyModeEnabled: Boolean = false,
    val topBarAlpha: Float = 0.3f
)
