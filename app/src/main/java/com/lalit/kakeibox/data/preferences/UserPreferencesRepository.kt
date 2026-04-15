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
            remindersEnabled = prefs[Keys.REMINDERS_ENABLED] ?: false,
            currencySymbol = prefs[Keys.CURRENCY_SYMBOL] ?: "¥",
            dateFormat = prefs[Keys.DATE_FORMAT] ?: "MMM dd, yyyy",
            appLanguage = AppLanguage.valueOf(prefs[Keys.APP_LANGUAGE] ?: AppLanguage.ENGLISH.name),
            biometricEnabled = prefs[Keys.BIOMETRIC_ENABLED] ?: false
        )
    }

    suspend fun setRemindersEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.REMINDERS_ENABLED] = enabled }
    }

    suspend fun setNavBarStyle(style: NavBarStyle) {
        dataStore.edit { it[Keys.NAV_BAR_STYLE] = style.name }
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
}
