package com.personal.kakeibox.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private object Keys {
    val DARK_THEME = stringPreferencesKey("dark_theme")
    val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    val NAV_BAR_STYLE = stringPreferencesKey("nav_bar_style")
    val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
    val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
    val DATE_FORMAT = stringPreferencesKey("date_format")
    val APP_LANGUAGE = stringPreferencesKey("app_language")
    val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    val TAB_ORDER = stringPreferencesKey("tab_order")
    val REST_DAYS = stringPreferencesKey("rest_days")
    val PRIVACY_MODE = booleanPreferencesKey("privacy_mode")
    val TOP_APP_BAR_BACKGROUND = stringPreferencesKey("top_app_bar_background")
    val THEME_STYLE = stringPreferencesKey("theme_style")
    val APP_FONT = stringPreferencesKey("app_font")
    val BACKDROP_PATTERN = stringPreferencesKey("backdrop_pattern")
    val GLOW_INTENSITY = stringPreferencesKey("glow_intensity")
    val CRT_FILTER_ENABLED = booleanPreferencesKey("crt_filter_enabled")
    val TOUCH_SYNESTHESIA = stringPreferencesKey("touch_synesthesia")
    val DYNAMIC_TONAL_STYLE = stringPreferencesKey("dynamic_tonal_style")
    val INTENSITY_PRESET = stringPreferencesKey("intensity_preset")
    val EARNINGS_CARD_SHAPE = stringPreferencesKey("earnings_card_shape")
    val NAV_ANIMATION = stringPreferencesKey("nav_animation")
    val SAVINGS_CARD_SHAPE = stringPreferencesKey("savings_card_shape")
    val REMITTANCE_CARD_SHAPE = stringPreferencesKey("remittance_card_shape")
}

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val themeSettings: Flow<ThemeSettings> = dataStore.data.map { prefs ->
        ThemeSettings(
            darkThemePreference = DarkThemePreference.fromStorage(prefs[Keys.DARK_THEME]),
            useDynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: true,
            navBarStyle = NavBarStyle.fromStorage(prefs[Keys.NAV_BAR_STYLE]),
            navAnimation = NavAnimationPreference.fromStorage(prefs[Keys.NAV_ANIMATION]),
            remindersEnabled = prefs[Keys.REMINDERS_ENABLED] ?: false,
            currencySymbol = prefs[Keys.CURRENCY_SYMBOL] ?: "¥",
            dateFormat = prefs[Keys.DATE_FORMAT] ?: "MMM dd, yyyy",
            appLanguage = AppLanguage.valueOf(prefs[Keys.APP_LANGUAGE] ?: AppLanguage.ENGLISH.name),
            biometricEnabled = prefs[Keys.BIOMETRIC_ENABLED] ?: false,
            tabOrder = run {
                val rawOrder = prefs[Keys.TAB_ORDER] ?: "salary,exercise,journeys,settings"
                var parsed = rawOrder.split(",").filter { it != "commute" && it != "spend" && it.isNotBlank() }
                if (!parsed.contains("exercise")) {
                    val list = parsed.toMutableList()
                    val settingsIndex = list.indexOf("settings")
                    if (settingsIndex != -1) {
                        list.add(settingsIndex, "exercise")
                    } else {
                        list.add("exercise")
                    }
                    parsed = list
                }
                if (!parsed.contains("journeys")) {
                    val list = parsed.toMutableList()
                    val settingsIndex = list.indexOf("settings")
                    if (settingsIndex != -1) {
                        list.add(settingsIndex, "journeys")
                    } else {
                        list.add("journeys")
                    }
                    parsed = list
                }
                parsed
            },
            restDays = (prefs[Keys.REST_DAYS] ?: "Saturday,Sunday").split(",").filter { it.isNotBlank() },
            privacyModeEnabled = prefs[Keys.PRIVACY_MODE] ?: false,
            topAppBarBackground = TopAppBarBackground.valueOf(
                prefs[Keys.TOP_APP_BAR_BACKGROUND] ?: TopAppBarBackground.PRIMARY_CONTAINER.name
            ),
            themeStyle = ThemeStyle.fromStorage(prefs[Keys.THEME_STYLE]),
            appFont = AppFont.fromStorage(prefs[Keys.APP_FONT]),
            backdropPattern = BackdropPattern.fromStorage(prefs[Keys.BACKDROP_PATTERN]),
            glowIntensity = GlowIntensity.fromStorage(prefs[Keys.GLOW_INTENSITY]),
            crtFilterEnabled = prefs[Keys.CRT_FILTER_ENABLED] ?: false,
            touchSynesthesia = TouchSynesthesia.fromStorage(prefs[Keys.TOUCH_SYNESTHESIA]),
            dynamicTonalStyle = DynamicTonalStyle.fromStorage(prefs[Keys.DYNAMIC_TONAL_STYLE]),
            intensityPreset = ColorIntensityPreset.fromStorage(prefs[Keys.INTENSITY_PRESET]),
            earningsCardShape = CardShapePreference.fromStorage(prefs[Keys.EARNINGS_CARD_SHAPE]),
            savingsCardShape = CardShapePreference.fromStorage(prefs[Keys.SAVINGS_CARD_SHAPE]),
            remittanceCardShape = CardShapePreference.fromStorage(prefs[Keys.REMITTANCE_CARD_SHAPE])
        )
    }

    suspend fun setRemindersEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.REMINDERS_ENABLED] = enabled }
    }

    suspend fun setNavBarStyle(style: NavBarStyle) {
        dataStore.edit { it[Keys.NAV_BAR_STYLE] = style.name }
    }

    suspend fun setNavAnimation(anim: NavAnimationPreference) {
        dataStore.edit { it[Keys.NAV_ANIMATION] = anim.name }
    }

    suspend fun setDarkThemePreference(value: DarkThemePreference) {
        dataStore.edit { it[Keys.DARK_THEME] = value.name }
    }

    suspend fun setUseDynamicColor(enabled: Boolean) {
        dataStore.edit { it[Keys.DYNAMIC_COLOR] = enabled }
    }

    suspend fun setCurrencySymbol(symbol: String) {
        dataStore.edit { it[Keys.CURRENCY_SYMBOL] = symbol }
    }

    suspend fun setDateFormat(format: String) {
        dataStore.edit { it[Keys.DATE_FORMAT] = format }
    }

    suspend fun setAppLanguage(language: AppLanguage) {
        dataStore.edit { it[Keys.APP_LANGUAGE] = language.name }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.BIOMETRIC_ENABLED] = enabled }
    }

    suspend fun setTabOrder(order: List<String>) {
        dataStore.edit { it[Keys.TAB_ORDER] = order.joinToString(",") }
    }

    suspend fun setPrivacyModeEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.PRIVACY_MODE] = enabled }
    }

    suspend fun setTopAppBarBackground(background: TopAppBarBackground) {
        dataStore.edit { it[Keys.TOP_APP_BAR_BACKGROUND] = background.name }
    }

    suspend fun setThemeStyle(style: ThemeStyle) {
        dataStore.edit { it[Keys.THEME_STYLE] = style.name }
    }

    suspend fun setAppFont(font: AppFont) {
        dataStore.edit { it[Keys.APP_FONT] = font.name }
    }

    suspend fun setBackdropPattern(pattern: BackdropPattern) {
        dataStore.edit { it[Keys.BACKDROP_PATTERN] = pattern.name }
    }

    suspend fun setGlowIntensity(intensity: GlowIntensity) {
        dataStore.edit { it[Keys.GLOW_INTENSITY] = intensity.name }
    }

    suspend fun setCrtFilterEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.CRT_FILTER_ENABLED] = enabled }
    }

    suspend fun setTouchSynesthesia(synesthesia: TouchSynesthesia) {
        dataStore.edit { it[Keys.TOUCH_SYNESTHESIA] = synesthesia.name }
    }

    suspend fun setDynamicTonalStyle(style: DynamicTonalStyle) {
        dataStore.edit { it[Keys.DYNAMIC_TONAL_STYLE] = style.name }
    }

    suspend fun setIntensityPreset(preset: ColorIntensityPreset) {
        dataStore.edit { it[Keys.INTENSITY_PRESET] = preset.name }
    }

    suspend fun setEarningsCardShape(shape: CardShapePreference) {
        dataStore.edit { it[Keys.EARNINGS_CARD_SHAPE] = shape.name }
    }

    suspend fun setSavingsCardShape(shape: CardShapePreference) {
        dataStore.edit { it[Keys.SAVINGS_CARD_SHAPE] = shape.name }
    }

    suspend fun setRemittanceCardShape(shape: CardShapePreference) {
        dataStore.edit { it[Keys.REMITTANCE_CARD_SHAPE] = shape.name }
    }

    suspend fun applyStylePreset(
        themeStyle: ThemeStyle,
        appFont: AppFont,
        backdropPattern: BackdropPattern,
        glowIntensity: GlowIntensity,
        crtFilterEnabled: Boolean,
        touchSynesthesia: TouchSynesthesia,
        darkThemePreference: DarkThemePreference,
        useDynamicColor: Boolean
    ) {
        dataStore.edit { prefs ->
            prefs[Keys.THEME_STYLE] = themeStyle.name
            prefs[Keys.APP_FONT] = appFont.name
            prefs[Keys.BACKDROP_PATTERN] = backdropPattern.name
            prefs[Keys.GLOW_INTENSITY] = glowIntensity.name
            prefs[Keys.CRT_FILTER_ENABLED] = crtFilterEnabled
            prefs[Keys.TOUCH_SYNESTHESIA] = touchSynesthesia.name
            prefs[Keys.DARK_THEME] = darkThemePreference.name
            prefs[Keys.DYNAMIC_COLOR] = useDynamicColor
        }
    }

    suspend fun setRestDays(days: List<String>) {
        dataStore.edit { prefs ->
            prefs[Keys.REST_DAYS] = days.joinToString(",")
        }
    }
}
