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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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
        dynamicColor = useDynamicColor
    ) {
        if (!themeSettings.biometricEnabled || isAuthenticated) {
            KakeiboXApp()
        } else {
            // Lock Screen placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // You could add a "Unlock" button here if the prompt was dismissed
            }
        }
    }
}
