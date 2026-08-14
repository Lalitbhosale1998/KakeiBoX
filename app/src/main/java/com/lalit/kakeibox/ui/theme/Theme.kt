package com.personal.kakeibox.ui.theme

import com.personal.kakeibox.data.preferences.ThemeSettings
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
import com.personal.kakeibox.data.preferences.ThemeFlavor
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
import androidx.compose.animation.core.spring
import com.personal.kakeibox.data.preferences.BackdropPattern
import com.personal.kakeibox.data.preferences.GlowIntensity
import com.personal.kakeibox.data.preferences.TouchSynesthesia

object ExpressivePhysics {
    fun <T> fluidSnappy() = spring<T>(dampingRatio = 0.82f, stiffness = 400f)
    fun <T> fluidBouncy() = spring<T>(dampingRatio = 0.65f, stiffness = 300f)
}

private val LightColors = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    error = Color(0xFFB3261E),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    surface = Color(0xFFFEF7FF),
    onSurface = Color(0xFF1D1B20),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    surfaceDim = Color(0xFFDED8E1),
    surfaceBright = Color(0xFFFEF7FF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF7F2FA),
    surfaceContainer = Color(0xFFF3EDF7),
    surfaceContainerHigh = Color(0xFFECE6F0),
    surfaceContainerHighest = Color(0xFFE6E0E9),
    background = Color(0xFFFEF7FF),
    onBackground = Color(0xFF1D1B20),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),
    error = Color(0xFFF2B8B5),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    surface = Color(0xFF141218),
    onSurface = Color(0xFFE6E0E9),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
    surfaceDim = Color(0xFF141218),
    surfaceBright = Color(0xFF3B383E),
    surfaceContainerLowest = Color(0xFF0F0D13),
    surfaceContainerLow = Color(0xFF1D1B20),
    surfaceContainer = Color(0xFF211F26),
    surfaceContainerHigh = Color(0xFF2B2930),
    surfaceContainerHighest = Color(0xFF36343B),
)

val LocalThemeStyle = staticCompositionLocalOf { ThemeStyle.M3_EXPRESSIVE }
val LocalTouchSynesthesia = staticCompositionLocalOf { TouchSynesthesia.SUBTLE }
val LocalGlowIntensity = staticCompositionLocalOf { GlowIntensity.SUBTLE }
val LocalThemeSettings = staticCompositionLocalOf { ThemeSettings() }


private fun getTypography(appFont: AppFont): androidx.compose.material3.Typography {
    val displayFontFamily = when (appFont) {
        AppFont.NUNITO -> com.personal.kakeibox.ui.theme.NunitoFontFamily
        AppFont.MONOSPACE -> FontFamily.Monospace
        AppFont.SYSTEM_SANS -> FontFamily.SansSerif
        AppFont.OUTFIT -> com.personal.kakeibox.ui.theme.OutfitFontFamily
        AppFont.PLAYFAIR -> com.personal.kakeibox.ui.theme.PlayfairFontFamily
        AppFont.GOOGLE_SANS_FLEX -> com.personal.kakeibox.ui.theme.GoogleSansFlexFontFamily
        AppFont.CLIMATE_CRISIS -> com.personal.kakeibox.ui.theme.ClimateCrisisFontFamily
        AppFont.LUCKIEST_GUY -> com.personal.kakeibox.ui.theme.LuckiestGuyFontFamily
        AppFont.DELA_GOTHIC_ONE -> com.personal.kakeibox.ui.theme.DelaGothicOneFontFamily
        AppFont.HACHI_MARU_POP -> com.personal.kakeibox.ui.theme.HachiMaruPopFontFamily
        AppFont.KOSUGI_MARU -> com.personal.kakeibox.ui.theme.KosugiMaruFontFamily
        AppFont.MOCHIY_POP_P_ONE -> com.personal.kakeibox.ui.theme.MochiyPopPOneFontFamily
        AppFont.POTTA_ONE -> com.personal.kakeibox.ui.theme.PottaOneFontFamily
        AppFont.RAMPART_ONE -> com.personal.kakeibox.ui.theme.RampartOneFontFamily
        AppFont.WDXL_LUBRIFONT_JPN -> com.personal.kakeibox.ui.theme.WDXLLubrifontJPNFontFamily
    }

    val isDecorativeFont = when (appFont) {
        AppFont.CLIMATE_CRISIS, AppFont.LUCKIEST_GUY -> true
        else -> false
    }

    val bodyFontFamily = if (isDecorativeFont) {
        com.personal.kakeibox.ui.theme.GoogleSansFlexFontFamily
    } else {
        displayFontFamily
    }

    val isJapaneseFont = when (appFont) {
        AppFont.DELA_GOTHIC_ONE, AppFont.HACHI_MARU_POP, AppFont.KOSUGI_MARU,
        AppFont.MOCHIY_POP_P_ONE, AppFont.POTTA_ONE, AppFont.RAMPART_ONE,
        AppFont.WDXL_LUBRIFONT_JPN -> true
        else -> false
    }

    val wideTransform = if (isJapaneseFont) {
        androidx.compose.ui.text.style.TextGeometricTransform(scaleX = 1.0f)
    } else {
        com.personal.kakeibox.ui.theme.ExpTitleTransform
    }
    return androidx.compose.material3.Typography(
        displayLarge = com.personal.kakeibox.ui.theme.Typography.displayLarge.copy(fontFamily = displayFontFamily, textGeometricTransform = wideTransform),
        displayMedium = com.personal.kakeibox.ui.theme.Typography.displayMedium.copy(fontFamily = displayFontFamily, textGeometricTransform = wideTransform),
        displaySmall = com.personal.kakeibox.ui.theme.Typography.displaySmall.copy(fontFamily = displayFontFamily, textGeometricTransform = wideTransform),
        headlineLarge = com.personal.kakeibox.ui.theme.Typography.headlineLarge.copy(fontFamily = displayFontFamily, textGeometricTransform = wideTransform),
        headlineMedium = com.personal.kakeibox.ui.theme.Typography.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = com.personal.kakeibox.ui.theme.Typography.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = com.personal.kakeibox.ui.theme.Typography.titleLarge.copy(fontFamily = bodyFontFamily),
        titleMedium = com.personal.kakeibox.ui.theme.Typography.titleMedium.copy(fontFamily = bodyFontFamily),
        titleSmall = com.personal.kakeibox.ui.theme.Typography.titleSmall.copy(fontFamily = bodyFontFamily),
        bodyLarge = com.personal.kakeibox.ui.theme.Typography.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = com.personal.kakeibox.ui.theme.Typography.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = com.personal.kakeibox.ui.theme.Typography.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = com.personal.kakeibox.ui.theme.Typography.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = com.personal.kakeibox.ui.theme.Typography.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = com.personal.kakeibox.ui.theme.Typography.labelSmall.copy(fontFamily = bodyFontFamily),
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
    isDark: Boolean = false,
    isPrimaryContainer: Boolean = false,
    primaryColor: Color = Color.Unspecified,
    containerColor: Color = Color.Unspecified,
    pattern: BackdropPattern = BackdropPattern.NONE,
    backgroundCanvasStyle: com.personal.kakeibox.data.preferences.BackgroundCanvasStyle = com.personal.kakeibox.data.preferences.BackgroundCanvasStyle.MONET_PASTEL
): Modifier = this.drawBehind {
    if (containerColor != Color.Unspecified) {
        drawRect(color = containerColor)
    }
}

fun Modifier.backdropPattern(pattern: BackdropPattern): Modifier = this

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
        primaryFixed = Color(colors.primaryFixed().getArgb(this)),
        primaryFixedDim = Color(colors.primaryFixedDim().getArgb(this)),
        onPrimaryFixed = Color(colors.onPrimaryFixed().getArgb(this)),
        onPrimaryFixedVariant = Color(colors.onPrimaryFixedVariant().getArgb(this)),
        secondaryFixed = Color(colors.secondaryFixed().getArgb(this)),
        secondaryFixedDim = Color(colors.secondaryFixedDim().getArgb(this)),
        onSecondaryFixed = Color(colors.onSecondaryFixed().getArgb(this)),
        onSecondaryFixedVariant = Color(colors.onSecondaryFixedVariant().getArgb(this)),
        tertiaryFixed = Color(colors.tertiaryFixed().getArgb(this)),
        tertiaryFixedDim = Color(colors.tertiaryFixedDim().getArgb(this)),
        onTertiaryFixed = Color(colors.onTertiaryFixed().getArgb(this)),
        onTertiaryFixedVariant = Color(colors.onTertiaryFixedVariant().getArgb(this))
    )
}

fun ColorScheme.scaleChroma(factor: Float): ColorScheme {
    if (factor == 1.0f) return this
    fun adjustColor(color: Color): Color {
        val hct = Hct.fromInt(color.toArgb())
        val newChroma = (hct.chroma * factor).coerceIn(0.0, 130.0)
        return Color(Hct.from(hct.hue, newChroma, hct.tone).toInt())
    }
    return copy(
        primary = adjustColor(primary),
        secondary = adjustColor(secondary),
        tertiary = adjustColor(tertiary),
        primaryContainer = adjustColor(primaryContainer),
        secondaryContainer = adjustColor(secondaryContainer),
        tertiaryContainer = adjustColor(tertiaryContainer)
    )
}

@Composable
fun KakeiboXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    themeStyle: ThemeStyle = ThemeStyle.M3_EXPRESSIVE,
    themeFlavor: ThemeFlavor = ThemeFlavor.DYNAMIC_MATERIAL,
    dynamicColorChromaScale: Float = 1.0f,
    appFont: AppFont = AppFont.NUNITO,
    touchSynesthesia: TouchSynesthesia = TouchSynesthesia.SUBTLE,
    glowIntensity: GlowIntensity = GlowIntensity.SUBTLE,
    dynamicTonalStyle: DynamicTonalStyle = DynamicTonalStyle.TONAL_SPOT,
    colorSeed: Color? = null,
    intensityPreset: ColorIntensityPreset = ColorIntensityPreset.SOFT,
    themeSettings: ThemeSettings = ThemeSettings(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val customFlavorScheme = ThemePalettes.getColorScheme(themeFlavor, darkTheme)
    val rawColorScheme = when {
        customFlavorScheme != null -> customFlavorScheme
        dynamicColor -> if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }
    
    val colorScheme = rawColorScheme

    // Per-flavor shape tokens: architectural (Shu-Nuri), pillow-round (O-Miki), default (all others)
    val shapes = when (themeFlavor) {
        ThemeFlavor.SHU_NURI -> Shapes(
            // Torii gate geometry — angular, architectural, minimal radius
            extraSmall = RoundedCornerShape(4.dp),
            small      = RoundedCornerShape(6.dp),
            medium     = RoundedCornerShape(8.dp),
            large      = RoundedCornerShape(10.dp),
            extraLarge = RoundedCornerShape(12.dp)
        )
        ThemeFlavor.O_MIKI -> Shapes(
            // Barrel silhouette — very round, pillow-like, celebratory
            extraSmall = RoundedCornerShape(16.dp),
            small      = RoundedCornerShape(20.dp),
            medium     = RoundedCornerShape(24.dp),
            large      = RoundedCornerShape(28.dp),
            extraLarge = RoundedCornerShape(32.dp)
        )
        ThemeFlavor.STHAPATYA -> Shapes(
            // Pancharatha step-pyramid temple geometry — 5-tiered stepped cut corners
            extraSmall = SthapatyaShapes.PancharathaBadgeShape,
            small      = SthapatyaShapes.PancharathaBadgeShape,
            medium     = SthapatyaShapes.PancharathaCardShape,
            large      = SthapatyaShapes.PancharathaCardShape,
            extraLarge = SthapatyaShapes.PancharathaCardShape
        )
        else -> KakeiboXShapes
    }
    val selectedFont = appFont
    val typography = getTypography(selectedFont)

    CompositionLocalProvider(
        LocalThemeStyle provides themeStyle,
        LocalTouchSynesthesia provides touchSynesthesia,
        LocalGlowIntensity provides glowIntensity,
        LocalThemeSettings provides themeSettings
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}