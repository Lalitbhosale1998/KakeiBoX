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
}

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val themeSettings: Flow<ThemeSettings> = dataStore.data.map { prefs ->
        ThemeSettings(
            darkThemePreference = DarkThemePreference.fromStorage(prefs[Keys.DARK_THEME]),
            useDynamicColor = prefs[Keys.DYNAMIC_COLOR] ?: true
        )
    }

    suspend fun setDarkThemePreference(value: DarkThemePreference) {
        dataStore.edit { it[Keys.DARK_THEME] = value.name }
    }

    suspend fun setUseDynamicColor(enabled: Boolean) {
        dataStore.edit { it[Keys.DYNAMIC_COLOR] = enabled }
    }
}
