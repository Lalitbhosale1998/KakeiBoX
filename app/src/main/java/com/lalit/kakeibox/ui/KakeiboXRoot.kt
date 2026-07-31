package com.personal.kakeibox.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.kakeibox.ui.settings.ThemeViewModel
import com.personal.kakeibox.ui.theme.KakeiboXTheme
import com.personal.kakeibox.ui.theme.terminalGridBackground
import com.personal.kakeibox.ui.theme.backdropPattern
import com.personal.kakeibox.ui.theme.crtScreenFilter
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.data.preferences.ColorIntensityPreset
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
    val dynamicSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val useDynamicColor = dynamicSupported && themeSettings.useDynamicColor

    val context = LocalContext.current
    val isAuthenticated by themeViewModel.isAuthenticated

    LaunchedEffect(themeSettings.biometricEnabled) {
        if (themeSettings.biometricEnabled && !isAuthenticated) {
            val activity = context as? FragmentActivity
            val executor = ContextCompat.getMainExecutor(context)
            if (activity != null) {
                themeViewModel.authenticate(activity, executor)
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
        val infiniteTransition = rememberInfiniteTransition(label = "ambient_bg")
        val baseColor = MaterialTheme.colorScheme.background
        val pulseColor = Color(ColorUtils.blendARGB(baseColor.toArgb(), MaterialTheme.colorScheme.primaryContainer.toArgb(), 0.15f))
        
        val ambientColor by infiniteTransition.animateColor(
            initialValue = baseColor,
            targetValue = pulseColor,
            animationSpec = infiniteRepeatable(
                animation = tween(4000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "ambient_color"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ambientColor)
                .backdropPattern(themeSettings.backdropPattern)
        ) {
            if (!themeSettings.isSetupComplete) {
                com.personal.kakeibox.ui.setup.SetupScreen(
                    themeSettings = themeSettings,
                    themeViewModel = themeViewModel,
                    onSetupComplete = { themeViewModel.setSetupComplete(true) }
                )
            } else if (!themeSettings.biometricEnabled || isAuthenticated) {
                val windowSizeClass = calculateWindowSizeClass(context as Activity)
                KakeiboXApp(windowSizeClass = windowSizeClass)
            } else {
                KakeiboXLockScreen(
                    themeSettings = themeSettings,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
