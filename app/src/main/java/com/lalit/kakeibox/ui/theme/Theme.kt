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
import com.google.android.material.color.utilities.Hct
import com.google.android.material.color.utilities.SchemeNeutral
import com.google.android.material.color.utilities.SchemeTonalSpot
import com.google.android.material.color.utilities.SchemeFidelity
import com.google.android.material.color.utilities.SchemeExpressive
import com.google.android.material.color.utilities.SchemeVibrant
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.personal.kakeibox.data.preferences.ThemeStyle
import com.personal.kakeibox.data.preferences.AppFont
import com.personal.kakeibox.data.preferences.DynamicTonalStyle
import com.personal.kakeibox.data.preferences.ColorIntensityPreset
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.animation.core.animateFloat
import com.personal.kakeibox.data.preferences.BackdropPattern
import com.personal.kakeibox.data.preferences.GlowIntensity
import com.personal.kakeibox.data.preferences.TouchSynesthesia

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
    background = Color(0xFFF5F6FB),
    onBackground = Color(0xFF191C20),
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
val LocalTouchSynesthesia = staticCompositionLocalOf { TouchSynesthesia.SUBTLE }
val LocalGlowIntensity = staticCompositionLocalOf { GlowIntensity.SUBTLE }


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

fun Modifier.terminalScanlines(): Modifier = this

fun Modifier.terminalGridBackground(): Modifier = this

fun Modifier.terminalButton(
    enabled: Boolean = true,
    backgroundColor: Color = Color(0xFFFF7E6B)
): Modifier = this

fun Modifier.glow(
    color: Color,
    radius: androidx.compose.ui.unit.Dp = 8.dp,
    intensity: GlowIntensity,
    shape: Shape = RoundedCornerShape(12.dp)
): Modifier = composed {
    if (intensity == GlowIntensity.OFF) return@composed this
    
    val alphaFactor = if (intensity == GlowIntensity.PULSING) {
        val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "glow_pulse")
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.8f,
            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                animation = androidx.compose.animation.core.tween(1500, easing = androidx.compose.animation.core.LinearEasing),
                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
            ),
            label = "alpha"
        )
        alpha
    } else {
        when (intensity) {
            GlowIntensity.SUBTLE -> 0.25f
            GlowIntensity.NEON -> 0.75f
            else -> 0.0f
        }
    }

    this.drawBehind {
        val shadowRadius = radius.toPx()
        val paint = Paint().asFrameworkPaint().apply {
            this.color = android.graphics.Color.TRANSPARENT
            setShadowLayer(
                shadowRadius,
                0f,
                0f,
                color.copy(alpha = alphaFactor).toArgb()
            )
        }
        
        drawIntoCanvas { canvas ->
            val outline = shape.createOutline(size, layoutDirection, this)
            when (outline) {
                is Outline.Rectangle -> {
                    canvas.nativeCanvas.drawRect(
                        0f, 0f, size.width, size.height, paint
                    )
                }
                is Outline.Rounded -> {
                    val rect = outline.roundRect
                    canvas.nativeCanvas.drawRoundRect(
                        rect.left, rect.top, rect.right, rect.bottom,
                        rect.topLeftCornerRadius.x, rect.topLeftCornerRadius.y,
                        paint
                    )
                }
                is Outline.Generic -> {
                    canvas.nativeCanvas.drawPath(
                        outline.path.asAndroidPath(), paint
                    )
                }
            }
        }
    }
}

fun Modifier.expressiveBackground(
    isDark: Boolean,
    isPrimaryContainer: Boolean,
    primaryColor: Color,
    containerColor: Color,
    pattern: BackdropPattern
): Modifier = this.drawBehind {
    if (isPrimaryContainer) {
        if (isDark) {
            // Draw premium dark mode gradient: deep primary accent fading to dark background
            val gradientBrush = androidx.compose.ui.graphics.Brush.radialGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.12f), // soft glowing accent
                    containerColor // dark surface
                ),
                center = Offset(size.width * 0.75f, size.height * 0.15f), // top-right glow
                radius = size.width * 1.3f
            )
            drawRect(brush = gradientBrush)
        } else {
            // Light mode: solid primaryContainer color
            drawRect(color = containerColor)
        }
    } else {
        // Standard style: solid surface color
        drawRect(color = containerColor)
    }

    // Now draw the patterns on top of the background!
    when (pattern) {
        BackdropPattern.NONE -> {}
        BackdropPattern.RADAR_DOTS -> {
            val dotColor = if (isDark) {
                primaryColor.copy(alpha = 0.12f) // glow dots matching the primary color!
            } else {
                Color(0xFF46C2B4).copy(alpha = 0.07f)
            }
            val spacing = 24.dp.toPx()
            var x = 0f
            while (x < size.width) {
                var y = 0f
                while (y < size.height) {
                    drawCircle(color = dotColor, radius = 1.5f.dp.toPx(), center = Offset(x, y))
                    y += spacing
                }
                x += spacing
            }
        }
        BackdropPattern.BLUEPRINT_GRID -> {
            val gridColor = if (isDark) {
                primaryColor.copy(alpha = 0.07f) // matching grid line glow!
            } else {
                Color(0xFF3B82F6).copy(alpha = 0.05f)
            }
            val spacing = 32.dp.toPx()
            var x = 0f
            while (x < size.width) {
                drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), 1f)
                x += spacing
            }
            var y = 0f
            while (y < size.height) {
                drawLine(gridColor, Offset(0f, y), Offset(size.width, y), 1f)
                y += spacing
            }
        }
        BackdropPattern.COCKPIT_STRIPES -> {
            val stripeColor = if (isDark) {
                primaryColor.copy(alpha = 0.05f)
            } else {
                Color(0xFFFFB359).copy(alpha = 0.035f)
            }
            val stripeWidth = 15.dp.toPx()
            val stripeGap = 30.dp.toPx()
            val path = Path()
            var xOffset = -size.height
            while (xOffset < size.width) {
                path.moveTo(xOffset, 0f)
                path.lineTo(xOffset + stripeWidth, 0f)
                path.lineTo(xOffset + stripeWidth + size.height, size.height)
                path.lineTo(xOffset + size.height, size.height)
                path.close()
                xOffset += stripeGap
            }
            drawPath(path = path, color = stripeColor)
        }
    }
}

fun Modifier.backdropPattern(pattern: BackdropPattern): Modifier = this.drawBehind {
    when (pattern) {
        BackdropPattern.NONE -> {}
        BackdropPattern.RADAR_DOTS -> {
            val dotColor = Color(0xFF46C2B4).copy(alpha = 0.07f)
            val spacing = 24.dp.toPx()
            var x = 0f
            while (x < size.width) {
                var y = 0f
                while (y < size.height) {
                    drawCircle(color = dotColor, radius = 1.5f.dp.toPx(), center = Offset(x, y))
                    y += spacing
                }
                x += spacing
            }
        }
        BackdropPattern.BLUEPRINT_GRID -> {
            val gridColor = Color(0xFF3B82F6).copy(alpha = 0.05f)
            val spacing = 32.dp.toPx()
            var x = 0f
            while (x < size.width) {
                drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), 1f)
                x += spacing
            }
            var y = 0f
            while (y < size.height) {
                drawLine(gridColor, Offset(0f, y), Offset(size.width, y), 1f)
                y += spacing
            }
        }
        BackdropPattern.COCKPIT_STRIPES -> {
            val stripeColor = Color(0xFFFFB359).copy(alpha = 0.035f)
            val stripeWidth = 15.dp.toPx()
            val stripeGap = 30.dp.toPx()
            val path = Path()
            var xOffset = -size.height
            while (xOffset < size.width) {
                path.moveTo(xOffset, 0f)
                path.lineTo(xOffset + stripeWidth, 0f)
                path.lineTo(xOffset + stripeWidth + size.height, size.height)
                path.lineTo(xOffset + size.height, size.height)
                path.close()
                xOffset += stripeGap
            }
            drawPath(path = path, color = stripeColor)
        }
    }
}

fun Modifier.crtScreenFilter(enabled: Boolean): Modifier = this

private fun com.google.android.material.color.utilities.DynamicScheme.toComposeColorScheme(): ColorScheme {
    val colors = com.google.android.material.color.utilities.MaterialDynamicColors()
    return ColorScheme(
        primary = Color(colors.primary().getArgb(this)),
        onPrimary = Color(colors.onPrimary().getArgb(this)),
        primaryContainer = Color(colors.primaryContainer().getArgb(this)),
        onPrimaryContainer = Color(colors.onPrimaryContainer().getArgb(this)),
        inversePrimary = Color(colors.inversePrimary().getArgb(this)),
        secondary = Color(colors.secondary().getArgb(this)),
        onSecondary = Color(colors.onSecondary().getArgb(this)),
        secondaryContainer = Color(colors.secondaryContainer().getArgb(this)),
        onSecondaryContainer = Color(colors.onSecondaryContainer().getArgb(this)),
        tertiary = Color(colors.tertiary().getArgb(this)),
        onTertiary = Color(colors.onTertiary().getArgb(this)),
        tertiaryContainer = Color(colors.tertiaryContainer().getArgb(this)),
        onTertiaryContainer = Color(colors.onTertiaryContainer().getArgb(this)),
        background = Color(colors.background().getArgb(this)),
        onBackground = Color(colors.onBackground().getArgb(this)),
        surface = Color(colors.surface().getArgb(this)),
        onSurface = Color(colors.onSurface().getArgb(this)),
        surfaceVariant = Color(colors.surfaceVariant().getArgb(this)),
        onSurfaceVariant = Color(colors.onSurfaceVariant().getArgb(this)),
        surfaceTint = Color(colors.primary().getArgb(this)),
        outline = Color(colors.outline().getArgb(this)),
        outlineVariant = Color(colors.outlineVariant().getArgb(this)),
        scrim = Color(colors.scrim().getArgb(this)),
        error = Color(colors.error().getArgb(this)),
        onError = Color(colors.onError().getArgb(this)),
        errorContainer = Color(colors.errorContainer().getArgb(this)),
        onErrorContainer = Color(colors.onErrorContainer().getArgb(this)),
        inverseSurface = Color(colors.inverseSurface().getArgb(this)),
        inverseOnSurface = Color(colors.inverseOnSurface().getArgb(this)),
        surfaceBright = Color(colors.surfaceBright().getArgb(this)),
        surfaceDim = Color(colors.surfaceDim().getArgb(this)),
        surfaceContainerLowest = Color(colors.surfaceContainerLowest().getArgb(this)),
        surfaceContainerLow = Color(colors.surfaceContainerLow().getArgb(this)),
        surfaceContainer = Color(colors.surfaceContainer().getArgb(this)),
        surfaceContainerHigh = Color(colors.surfaceContainerHigh().getArgb(this)),
        surfaceContainerHighest = Color(colors.surfaceContainerHighest().getArgb(this)),
    )
}

private fun getWallpaperSeedColor(context: android.content.Context, fallbackColor: Color): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        try {
            val systemAccentResId = context.resources.getIdentifier("system_accent1_500", "color", "android")
            if (systemAccentResId != 0) {
                return context.getColor(systemAccentResId)
            }
        } catch (e: Exception) {
            // Fallback
        }
    }
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            val wallpaperManager = context.getSystemService(android.content.Context.WALLPAPER_SERVICE) as? android.app.WallpaperManager
            val wallpaperColors = wallpaperManager?.getWallpaperColors(android.app.WallpaperManager.FLAG_SYSTEM)
            if (wallpaperColors != null) {
                return wallpaperColors.primaryColor.toArgb()
            }
        } catch (e: Exception) {
            // Fallback
        }
    }
    
    return fallbackColor.toArgb()
}


@Composable
fun KakeiboXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    themeStyle: ThemeStyle = ThemeStyle.M3_EXPRESSIVE,
    appFont: AppFont = AppFont.NUNITO,
    touchSynesthesia: TouchSynesthesia = TouchSynesthesia.SUBTLE,
    glowIntensity: GlowIntensity = GlowIntensity.SUBTLE,
    dynamicTonalStyle: DynamicTonalStyle = DynamicTonalStyle.TONAL_SPOT,
    colorSeed: Color? = null,
    intensityPreset: ColorIntensityPreset = ColorIntensityPreset.SOFT,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor -> {
            val fallbackColor = if (darkTheme) Color(0xFFAAC7FF) else Color(0xFF1565C0)
            val seedInt = colorSeed?.toArgb() ?: getWallpaperSeedColor(context, fallbackColor)
            val hct = Hct.fromInt(seedInt)
            val dynamicScheme = when (intensityPreset) {
                ColorIntensityPreset.NEUTRAL -> {
                    val mutedHct = Hct.from(hct.hue, 12.0, hct.tone)
                    SchemeFidelity(mutedHct, darkTheme, 0.0)
                }
                ColorIntensityPreset.SOFT -> SchemeTonalSpot(hct, darkTheme, 0.0)
                ColorIntensityPreset.BRIGHT -> SchemeFidelity(hct, darkTheme, 0.0)
                ColorIntensityPreset.BOLD -> SchemeVibrant(hct, darkTheme, 0.0)
            }
            val scheme = dynamicScheme.toComposeColorScheme()
            if (!darkTheme && scheme.background == Color.White) {
                scheme.copy(background = Color(0xFFF5F6FB))
            } else {
                scheme
            }
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val shapes = KakeiboXShapes
    val selectedFont = appFont
    val typography = getTypography(selectedFont)

    CompositionLocalProvider(
        LocalThemeStyle provides themeStyle,
        LocalTouchSynesthesia provides touchSynesthesia,
        LocalGlowIntensity provides glowIntensity
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}