package com.personal.kakeibox.ui

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.theme.KakeiboXTheme
import com.personal.kakeibox.ui.theme.backdropPattern
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun KakeiboXAppRoot() {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val themeSettings by themeViewModel.themeSettings.collectAsStateWithLifecycle()
    val systemDark = isSystemInDarkTheme()
    val darkTheme = themeSettings.darkThemePreference.isDark(systemDark)
    val useDynamicColor = themeSettings.useDynamicColor

    val context = LocalContext.current
    val isAuthenticated by themeViewModel.isAuthenticated

    LaunchedEffect(themeSettings.biometricEnabled) {
        if (themeSettings.biometricEnabled && !isAuthenticated) {
            val activity = context as? androidx.fragment.app.FragmentActivity
            if (activity != null) {
                themeViewModel.authenticate(activity, context.mainExecutor)
            }
        }
    }

    KakeiboXTheme(
        darkTheme = darkTheme,
        dynamicColor = useDynamicColor,
        themeStyle = themeSettings.themeStyle,
        themeFlavor = themeSettings.themeFlavor,
        dynamicColorChromaScale = themeSettings.dynamicColorChromaScale,
        appFont = themeSettings.appFont,
        touchSynesthesia = themeSettings.touchSynesthesia,
        glowIntensity = themeSettings.glowIntensity,
        dynamicTonalStyle = themeSettings.dynamicTonalStyle,
        intensityPreset = themeSettings.intensityPreset,
        themeSettings = themeSettings
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .backdropPattern(themeSettings.backdropPattern)
        ) {
            if (!themeSettings.isSetupComplete) {
                com.personal.kakeibox.ui.setup.SetupScreen(
                    themeSettings = themeSettings,
                    themeViewModel = themeViewModel,
                    onSetupComplete = { themeViewModel.setSetupComplete(true) }
                )
            } else {
                val windowSizeClass = calculateWindowSizeClass(context as Activity)
                KakeiboXApp(windowSizeClass = windowSizeClass)
            }
        }
    }
}
