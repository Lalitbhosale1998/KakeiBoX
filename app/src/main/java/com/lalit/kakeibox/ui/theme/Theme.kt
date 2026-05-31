package com.personal.kakeibox.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.data.preferences.AppFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke

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
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    surface = Color(0xFFF8F9FF),
    onSurface = Color(0xFF191C20),
    surfaceVariant = Color(0xFFE0E2EC),
    onSurfaceVariant = Color(0xFF43474E),
    outline = Color(0xFF73777F),
    outlineVariant = Color(0xFFC4C6D0),
    surfaceDim = Color(0xFFDAD9E0),
    surfaceBright = Color(0xFFF8F9FF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF2F3FA),
    surfaceContainer = Color(0xFFECEEF4),
    surfaceContainerHigh = Color(0xFFE7E8EE),
    surfaceContainerHighest = Color(0xFFE1E2E8),
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
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    surface = Color(0xFF111318),
    onSurface = Color(0xFFE2E2E9),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C6CF),
    outline = Color(0xFF8D9199),
    outlineVariant = Color(0xFF43474E),
    surfaceDim = Color(0xFF111318),
    surfaceBright = Color(0xFF37393E),
    surfaceContainerLowest = Color(0xFF0C0E13),
    surfaceContainerLow = Color(0xFF1A1C22),
    surfaceContainer = Color(0xFF1E2026),
    surfaceContainerHigh = Color(0xFF282A31),
    surfaceContainerHighest = Color(0xFF33353C),
)

val LocalThemeStyle = staticCompositionLocalOf { ThemeStyle.M3_EXPRESSIVE }

private val RetroSpaceColors = darkColorScheme(
    primary = Color(0xFFFF7E6B),       // Electric Coral
    onPrimary = Color(0xFF0C1020),     // Deep navy text
    primaryContainer = Color(0xFF1D264A),
    onPrimaryContainer = Color(0xFFFF7E6B),
    secondary = Color(0xFF46C2B4),     // Neon Cyan
    onSecondary = Color(0xFF0C1020),
    secondaryContainer = Color(0xFF142B34),
    onSecondaryContainer = Color(0xFF46C2B4),
    tertiary = Color(0xFFFFB359),      // Warm Amber
    onTertiary = Color(0xFF0C1020),
    tertiaryContainer = Color(0xFF2C2415),
    onTertiaryContainer = Color(0xFFFFB359),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0C1020),    // Dark Space Navy
    surface = Color(0xFF0C1020),
    onBackground = Color(0xFFE2E7FF),  // Cool Ice Blue
    onSurface = Color(0xFFE2E7FF),
    surfaceVariant = Color(0xFF13182E), // Deep Mecha Card Blue
    onSurfaceVariant = Color(0xFF8898C8), // Muted Blue-Gray
    outline = Color(0xFF46C2B4),       // Cyan outline
    outlineVariant = Color(0xFFFF7E6B),
    surfaceDim = Color(0xFF090C19),
    surfaceBright = Color(0xFF1C223E),
    surfaceContainerLowest = Color(0xFF060811),
    surfaceContainerLow = Color(0xFF101427),
    surfaceContainer = Color(0xFF13182E),
    surfaceContainerHigh = Color(0xFF171D3A),
    surfaceContainerHighest = Color(0xFF1B2246),
)

private val RetroSpaceShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp),
    extraLarge = RoundedCornerShape(16.dp)
)

private fun getTypography(appFont: AppFont): androidx.compose.material3.Typography {
    val fontFamily = when (appFont) {
        AppFont.NUNITO -> com.personal.kakeibox.ui.theme.NunitoFontFamily
        AppFont.MONOSPACE -> FontFamily.Monospace
        AppFont.SYSTEM_SANS -> FontFamily.SansSerif
        AppFont.OUTFIT -> com.personal.kakeibox.ui.theme.OutfitFontFamily
        AppFont.PLAYFAIR -> com.personal.kakeibox.ui.theme.PlayfairFontFamily
    }
    return androidx.compose.material3.Typography(
        displayLarge = com.personal.kakeibox.ui.theme.Typography.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = com.personal.kakeibox.ui.theme.Typography.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = com.personal.kakeibox.ui.theme.Typography.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = com.personal.kakeibox.ui.theme.Typography.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = com.personal.kakeibox.ui.theme.Typography.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = com.personal.kakeibox.ui.theme.Typography.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = com.personal.kakeibox.ui.theme.Typography.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = com.personal.kakeibox.ui.theme.Typography.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = com.personal.kakeibox.ui.theme.Typography.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = com.personal.kakeibox.ui.theme.Typography.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = com.personal.kakeibox.ui.theme.Typography.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = com.personal.kakeibox.ui.theme.Typography.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = com.personal.kakeibox.ui.theme.Typography.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = com.personal.kakeibox.ui.theme.Typography.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = com.personal.kakeibox.ui.theme.Typography.labelSmall.copy(fontFamily = fontFamily),
    )
}

fun Modifier.terminalScanlines(): Modifier = composed {
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.RETRO_SPACE
    if (!isSpaceTerminal) return@composed this
    
    this.drawWithContent {
        drawContent()
        
        // Draw horizontal CRT scanlines
        val scanlineSpacing = 6.dp.toPx()
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = Color(0xFF46C2B4).copy(alpha = 0.05f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
            y += scanlineSpacing
        }
        
        // Draw tactical corner bracket outlines
        val bracketSize = 8.dp.toPx()
        val bracketColor = Color(0xFF46C2B4).copy(alpha = 0.4f)
        val stroke = 1.dp.toPx()
        
        // Top-left
        drawLine(bracketColor, Offset(0f, 0f), Offset(bracketSize, 0f), stroke)
        drawLine(bracketColor, Offset(0f, 0f), Offset(0f, bracketSize), stroke)
        
        // Top-right
        drawLine(bracketColor, Offset(size.width, 0f), Offset(size.width - bracketSize, 0f), stroke)
        drawLine(bracketColor, Offset(size.width, 0f), Offset(size.width, bracketSize), stroke)
        
        // Bottom-left
        drawLine(bracketColor, Offset(0f, size.height), Offset(bracketSize, size.height), stroke)
        drawLine(bracketColor, Offset(0f, size.height), Offset(0f, size.height - bracketSize), stroke)
        
        // Bottom-right
        drawLine(bracketColor, Offset(size.width, size.height), Offset(size.width - bracketSize, size.height), stroke)
        drawLine(bracketColor, Offset(size.width, size.height), Offset(size.width, size.height - bracketSize), stroke)
    }
}

fun Modifier.terminalGridBackground(): Modifier = composed {
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.RETRO_SPACE
    if (!isSpaceTerminal) return@composed this
    this.drawBehind {
        val gridSpacing = 32.dp.toPx()
        val lineColor = Color(0xFF46C2B4).copy(alpha = 0.04f)
        
        // Vertical lines
        var x = 0f
        while (x < size.width) {
            drawLine(lineColor, Offset(x, 0f), Offset(x, size.height), 1f)
            x += gridSpacing
        }
        
        // Horizontal lines
        var y = 0f
        while (y < size.height) {
            drawLine(lineColor, Offset(0f, y), Offset(size.width, y), 1f)
            y += gridSpacing
        }
    }
}

fun Modifier.terminalButton(
    enabled: Boolean = true,
    backgroundColor: Color = Color(0xFFFF7E6B) // Electric Coral
): Modifier = composed {
    val isSpaceTerminal = LocalThemeStyle.current == ThemeStyle.RETRO_SPACE
    if (!isSpaceTerminal) return@composed this
    
    this.drawBehind {
        // Draw mecha main fill
        val fillBrush = if (enabled) {
            Brush.verticalGradient(
                colors = listOf(
                    backgroundColor,
                    backgroundColor.copy(alpha = 0.85f)
                )
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    backgroundColor.copy(alpha = 0.3f),
                    backgroundColor.copy(alpha = 0.2f)
                )
            )
        }
        
        drawRoundRect(
            brush = fillBrush,
            size = size,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx())
        )
        
        if (enabled) {
            // 1. Draw horizontal speed lines in the background
            val lineCount = 4
            val startY = size.height * 0.5f
            val lineSpacing = 3.dp.toPx()
            for (i in 0 until lineCount) {
                val y = startY + i * lineSpacing
                val alpha = 0.15f + (i * 0.05f)
                drawLine(
                    color = Color(0xFF0C1020).copy(alpha = alpha), // Dark lines
                    start = Offset(6.dp.toPx(), y),
                    end = Offset(size.width - 6.dp.toPx(), y),
                    strokeWidth = 1.5.dp.toPx()
                )
            }
            
            // 2. Draw glowing neon stripe at the top edge
            drawLine(
                color = Color(0xFFFFB359), // Warm Amber top stripe
                start = Offset(12.dp.toPx(), 4.dp.toPx()),
                end = Offset(size.width - 12.dp.toPx(), 4.dp.toPx()),
                strokeWidth = 2.dp.toPx()
            )
            
            // 3. Draw vertical mecha tactical side stripes
            val sideStripeColor = Color(0xFF46C2B4) // Neon Cyan side bars
            // Left
            drawLine(
                color = sideStripeColor,
                start = Offset(1.dp.toPx(), size.height * 0.2f),
                end = Offset(1.dp.toPx(), size.height * 0.8f),
                strokeWidth = 3.dp.toPx()
            )
            // Right
            drawLine(
                color = sideStripeColor,
                start = Offset(size.width - 1.dp.toPx(), size.height * 0.2f),
                end = Offset(size.width - 1.dp.toPx(), size.height * 0.8f),
                strokeWidth = 3.dp.toPx()
            )
        }
    }
    .drawWithContent {
        drawContent()
        if (enabled) {
            // Double border line
            drawRoundRect(
                color = Color(0xFF0C1020).copy(alpha = 0.25f),
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
            )
        }
    }
}

@Composable
fun KakeiboXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    themeStyle: ThemeStyle = ThemeStyle.M3_EXPRESSIVE,
    appFont: AppFont = AppFont.NUNITO,
    content: @Composable () -> Unit
) {
    val isRetroSpace = themeStyle == ThemeStyle.RETRO_SPACE
    
    val colorScheme = when {
        isRetroSpace -> RetroSpaceColors
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val shapes = if (isRetroSpace) RetroSpaceShapes else KakeiboXShapes
    val selectedFont = if (isRetroSpace && appFont == AppFont.NUNITO) AppFont.MONOSPACE else appFont
    val typography = getTypography(selectedFont)

    CompositionLocalProvider(
        LocalThemeStyle provides themeStyle
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}