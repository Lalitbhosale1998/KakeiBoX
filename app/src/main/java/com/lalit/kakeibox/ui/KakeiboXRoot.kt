package com.personal.kakeibox.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.theme.KakeiboXTheme

@Composable
fun KakeiboXAppRoot() {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val systemDark = isSystemInDarkTheme()
    val darkTheme = themeSettings.darkThemePreference.isDark(systemDark)
    val dynamicSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val useDynamicColor = dynamicSupported && themeSettings.useDynamicColor

    KakeiboXTheme(
        darkTheme = darkTheme,
        dynamicColor = useDynamicColor
    ) {
        KakeiboXApp()
    }
}
