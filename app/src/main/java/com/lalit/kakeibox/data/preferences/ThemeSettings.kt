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

enum class CardShapePreference {
    DEFAULT, SEMICIRCLE, PILL, CLAMSHELL, SLANTED, SQUARE;

    companion object {
        fun fromStorage(value: String?): CardShapePreference {
            return try {
                value?.let { valueOf(it) } ?: PILL
            } catch (e: Exception) {
                PILL
            }
        }
    }
}

enum class NavAnimationPreference {
    MORPHING, ELASTIC_JELLY, LIQUID_RIPPLE, ARCADE_3D;

    companion object {
        fun fromStorage(value: String?): NavAnimationPreference {
            return try {
                value?.let { valueOf(it) } ?: MORPHING
            } catch (e: Exception) {
                MORPHING
            }
        }
    }
}

data class ThemeSettings(
    val darkThemePreference: DarkThemePreference = DarkThemePreference.SYSTEM,
    val useDynamicColor: Boolean = true,
    val navBarStyle: NavBarStyle = NavBarStyle.FLOATING,
    val navAnimation: NavAnimationPreference = NavAnimationPreference.MORPHING,
    val remindersEnabled: Boolean = false,
    val currencySymbol: String = "¥",
    val dateFormat: String = "MMM dd, yyyy",
    val appLanguage: AppLanguage = AppLanguage.ENGLISH,
    val biometricEnabled: Boolean = false,
    val tabOrder: List<String> = listOf("salary", "exercise", "kotoba", "settings"),
    val restDays: List<String> = listOf("Saturday", "Sunday"),
    val privacyModeEnabled: Boolean = false,
    val topAppBarBackground: TopAppBarBackground = TopAppBarBackground.PRIMARY_CONTAINER,
    val themeStyle: ThemeStyle = ThemeStyle.M3_EXPRESSIVE,
    val appFont: AppFont = AppFont.NUNITO,
    val backdropPattern: BackdropPattern = BackdropPattern.NONE,
    val glowIntensity: GlowIntensity = GlowIntensity.SUBTLE,
    val crtFilterEnabled: Boolean = false,
    val touchSynesthesia: TouchSynesthesia = TouchSynesthesia.SUBTLE,
    val dynamicTonalStyle: DynamicTonalStyle = DynamicTonalStyle.TONAL_SPOT,
    val intensityPreset: ColorIntensityPreset = ColorIntensityPreset.SOFT,
    val themeFlavor: ThemeFlavor = ThemeFlavor.DYNAMIC_MATERIAL,
    val dynamicColorChromaScale: Float = 1.0f,
    val earningsCardShape: CardShapePreference = CardShapePreference.PILL,
    val savingsCardShape: CardShapePreference = CardShapePreference.PILL,
    val remittanceCardShape: CardShapePreference = CardShapePreference.PILL,
    val isSetupComplete: Boolean = true
)

