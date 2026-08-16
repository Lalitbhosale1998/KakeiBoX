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
    DEFAULT, SEMICIRCLE, PILL, CLAMSHELL, SLANTED, SQUARE, COOKIE, BUN;

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

enum class BackgroundCanvasStyle {
    MONET_PASTEL, FULL_VIBRANT_PRIMARY, SOLID_SURFACE;

    companion object {
        fun fromStorage(value: String?): BackgroundCanvasStyle {
            return try {
                value?.let { valueOf(it) } ?: MONET_PASTEL
            } catch (e: Exception) {
                MONET_PASTEL
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
    val tabOrder: List<String> = listOf("home", "salary", "exercise", "shlok", "kotoba", "settings"),
    val hiddenTabs: Set<String> = emptySet(),
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
    val backgroundCanvasStyle: BackgroundCanvasStyle = BackgroundCanvasStyle.MONET_PASTEL,
    val isSetupComplete: Boolean = true,
    val medicationStartDate: String = "2026-08-17",
    val medicationBreakfastTime: String = "08:30",
    val medicationLunchTime: String = "13:15",
    val medicationDinnerTime: String = "20:45",
    val homeWidgetOrder: List<String> = listOf("hero_gauge", "bento_grid", "medication", "action_dock", "snapshot_row")
)

