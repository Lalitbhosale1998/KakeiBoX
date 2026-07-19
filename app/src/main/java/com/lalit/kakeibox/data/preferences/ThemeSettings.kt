package com.personal.kakeibox.data.preferences

enum class AppLanguage {
    ENGLISH, JAPANESE
}

enum class ColorIntensityPreset {
    NEUTRAL, SOFT, BRIGHT, BOLD;

    companion object {
        fun fromStorage(value: String?): ColorIntensityPreset {
            return try {
                value?.let { valueOf(it) } ?: SOFT
            } catch (e: Exception) {
                SOFT
            }
        }
    }
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
    val tabOrder: List<String> = listOf("salary", "exercise", "settings"),
    val restDays: List<String> = listOf("Saturday", "Sunday"),
    val privacyModeEnabled: Boolean = false,
    val topAppBarBackground: TopAppBarBackground = TopAppBarBackground.SURFACE,
    val themeStyle: ThemeStyle = ThemeStyle.M3_EXPRESSIVE,
    val appFont: AppFont = AppFont.NUNITO,
    val backdropPattern: BackdropPattern = BackdropPattern.NONE,
    val glowIntensity: GlowIntensity = GlowIntensity.SUBTLE,
    val crtFilterEnabled: Boolean = false,
    val touchSynesthesia: TouchSynesthesia = TouchSynesthesia.SUBTLE,
    val dynamicTonalStyle: DynamicTonalStyle = DynamicTonalStyle.TONAL_SPOT,
    val intensityPreset: ColorIntensityPreset = ColorIntensityPreset.SOFT
)
