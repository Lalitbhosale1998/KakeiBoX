package com.personal.kakeibox.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = Color(0xFF001B45),
    secondary = Color(0xFF2E7D32),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFB9F6CA),
    onSecondaryContainer = Color(0xFF00210A),
    tertiary = Color(0xFFE65100),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDCC4),
    onTertiaryContainer = Color(0xFF321200),
    error = Color(0xFFBA1A1A),
    surface = Color(0xFFF8F9FF),
    onSurface = Color(0xFF191C20),
    surfaceVariant = Color(0xFFE0E2EC),
    onSurfaceVariant = Color(0xFF43474E),
    outline = Color(0xFF73777F),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFAAC7FF),
    onPrimary = Color(0xFF002F6C),
    primaryContainer = Color(0xFF004798),
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondary = Color(0xFF6EF094),
    onSecondary = Color(0xFF003918),
    secondaryContainer = Color(0xFF005226),
    onSecondaryContainer = Color(0xFFB9F6CA),
    tertiary = Color(0xFFFFB680),
    onTertiary = Color(0xFF502200),
    tertiaryContainer = Color(0xFF6F3300),
    onTertiaryContainer = Color(0xFFFFDCC4),
    error = Color(0xFFFFB4AB),
    surface = Color(0xFF111318),
    onSurface = Color(0xFFE2E2E9),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C6CF),
    outline = Color(0xFF8D9199),
)

@Composable
fun KakeiboXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = KakeiboXShapes,
        content = content
    )
}