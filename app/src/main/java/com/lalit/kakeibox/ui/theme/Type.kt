@file:OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)

package com.personal.kakeibox.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.personal.kakeibox.R

val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val FredokaFont = GoogleFont("Fredoka")

val FredokaFontFamily = FontFamily(
    Font(googleFont = FredokaFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = FredokaFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = FredokaFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = FredokaFont, fontProvider = fontProvider, weight = FontWeight.Bold)
)

val ComfortaaFont = GoogleFont("Comfortaa")

val ComfortaaFontFamily = FontFamily(
    Font(googleFont = ComfortaaFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = ComfortaaFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = ComfortaaFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = ComfortaaFont, fontProvider = fontProvider, weight = FontWeight.Bold)
)

val NunitoFontFamily = FontFamily(
    Font(resId = R.font.nunito_variable, weight = FontWeight.Normal),
    Font(resId = R.font.nunito_variable, weight = FontWeight.Medium),
    Font(resId = R.font.nunito_variable, weight = FontWeight.SemiBold),
    Font(resId = R.font.nunito_variable, weight = FontWeight.Bold),
    Font(resId = R.font.nunito_variable, weight = FontWeight.ExtraBold),
    Font(resId = R.font.nunito_variable, weight = FontWeight.Black),
)

val OutfitFontFamily = FontFamily(
    Font(resId = R.font.outfit_variable, weight = FontWeight.Normal),
    Font(resId = R.font.outfit_variable, weight = FontWeight.Medium),
    Font(resId = R.font.outfit_variable, weight = FontWeight.SemiBold),
    Font(resId = R.font.outfit_variable, weight = FontWeight.Bold),
)

val PlayfairFontFamily = FontFamily(
    Font(resId = R.font.playfair_variable, weight = FontWeight.Normal),
    Font(resId = R.font.playfair_variable, weight = FontWeight.Medium),
    Font(resId = R.font.playfair_variable, weight = FontWeight.SemiBold),
    Font(resId = R.font.playfair_variable, weight = FontWeight.Bold),
)

// 🌟 Google Sans Flex Variable with Maximum Roundness (ROND = 100f)
val GoogleSansFlexFontFamily = FontFamily(
    Font(
        resId = R.font.gflex_variable,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Normal.weight),
            FontVariation.Setting("ROND", 100f)
        )
    ),
    Font(
        resId = R.font.gflex_variable,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Medium.weight),
            FontVariation.Setting("ROND", 100f)
        )
    ),
    Font(
        resId = R.font.gflex_variable,
        weight = FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.SemiBold.weight),
            FontVariation.Setting("ROND", 100f)
        )
    ),
    Font(
        resId = R.font.gflex_variable,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Bold.weight),
            FontVariation.Setting("ROND", 100f)
        )
    ),
    Font(
        resId = R.font.gflex_variable,
        weight = FontWeight.Black,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Black.weight),
            FontVariation.Setting("ROND", 100f)
        )
    )
)

// 🔥 Genre / Montserrat Variable Font Family for Display Headers
val GenreFontFamily = FontFamily(
    Font(resId = R.font.genre_variable, weight = FontWeight.Normal),
    Font(resId = R.font.genre_variable, weight = FontWeight.Medium),
    Font(resId = R.font.genre_variable, weight = FontWeight.SemiBold),
    Font(resId = R.font.genre_variable, weight = FontWeight.Bold),
    Font(resId = R.font.genre_variable, weight = FontWeight.Black),
)

// ↔️ Ultra-Wide Geometric Scale Transform (PixelPlayer style)
val ExpTitleTransform = TextGeometricTransform(scaleX = 1.35f)

// M3 Expressive typography — Dual Font architecture
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = NunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)