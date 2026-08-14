package com.personal.kakeibox.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.personal.kakeibox.data.preferences.ThemeFlavor

object ThemePalettes {

    // ── 1. Midnight Obsidian (Deep OLED Cyberpunk Glow) ───────────
    val MidnightObsidianDark: ColorScheme = darkColorScheme(
        primary = Color(0xFF00F2FE),            // Electric Neon Cyan
        onPrimary = Color(0xFF00363A),
        primaryContainer = Color(0xFF004F56),
        onPrimaryContainer = Color(0xFFB1F5FF),
        secondary = Color(0xFF8A2BE2),          // Deep Violet
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFF3C096C),
        onSecondaryContainer = Color(0xFFE0AAFF),
        tertiary = Color(0xFF00E676),           // Mint Pulse
        onTertiary = Color(0xFF003819),
        tertiaryContainer = Color(0xFF005227),
        onTertiaryContainer = Color(0xFFB9FFCE),
        background = Color(0xFF090C10),         // Ultra Deep Charcoal
        onBackground = Color(0xFFF0F4F8),
        surface = Color(0xFF0F141C),            // Obsidian Surface
        onSurface = Color(0xFFF0F4F8),
        surfaceVariant = Color(0xFF1E2634),
        onSurfaceVariant = Color(0xFF94A3B8),
        surfaceContainerLowest = Color(0xFF06080C),
        surfaceContainerLow = Color(0xFF0C1017),
        surfaceContainer = Color(0xFF131A24),
        surfaceContainerHigh = Color(0xFF1A2330),
        surfaceContainerHighest = Color(0xFF222C3D),
        outline = Color(0xFF334155),
        outlineVariant = Color(0xFF1E293B)
    )

    val MidnightObsidianLight: ColorScheme = lightColorScheme(
        primary = Color(0xFF00838F),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFE0F7FA),
        onPrimaryContainer = Color(0xFF00272B),
        secondary = Color(0xFF6A1B9A),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFF3E5F5),
        onSecondaryContainer = Color(0xFF3A005C),
        tertiary = Color(0xFF2E7D32),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xE8F5E9),
        onTertiaryContainer = Color(0xFF0A360E),
        background = Color(0xFFF8FAFC),
        onBackground = Color(0xFF0F172A),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF0F172A),
        surfaceVariant = Color(0xFFE2E8F0),
        onSurfaceVariant = Color(0xFF475569),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF1F5F9),
        surfaceContainer = Color(0xFFE2E8F0),
        surfaceContainerHigh = Color(0xFFCBD5E1),
        surfaceContainerHighest = Color(0xFF94A3B8),
        outline = Color(0xFF64748B),
        outlineVariant = Color(0xFFCBD5E1)
    )

    // ── 2. Emerald Zen (Organic Financial Forest) ─────────────────
    val EmeraldZenDark: ColorScheme = darkColorScheme(
        primary = Color(0xFF10B981),            // Emerald Green
        onPrimary = Color(0xFF003822),
        primaryContainer = Color(0xFF005234),
        onPrimaryContainer = Color(0xFFA7F3D0),
        secondary = Color(0xFFF59E0B),          // Warm Gold
        onSecondary = Color(0xFF422006),
        secondaryContainer = Color(0xFF78350F),
        onSecondaryContainer = Color(0xFFFDE68A),
        tertiary = Color(0xFF06B6D4),           // Cyan Stream
        onTertiary = Color(0xFF003642),
        tertiaryContainer = Color(0xFF004E5F),
        onTertiaryContainer = Color(0xFFCFFAFE),
        background = Color(0xFF07140E),         // Deep Forest Obsidian
        onBackground = Color(0xFFECFDF5),
        surface = Color(0xFF0D2118),            // Dark Sage Container
        onSurface = Color(0xFFECFDF5),
        surfaceVariant = Color(0xFF183B2C),
        onSurfaceVariant = Color(0xFF94A3B8),
        surfaceContainerLowest = Color(0xFF040D09),
        surfaceContainerLow = Color(0xFF0A1B13),
        surfaceContainer = Color(0xFF11281E),
        surfaceContainerHigh = Color(0xFF183528),
        surfaceContainerHighest = Color(0xFF204233),
        outline = Color(0xFF2D5A47),
        outlineVariant = Color(0xFF1B3D2F)
    )

    val EmeraldZenLight: ColorScheme = lightColorScheme(
        primary = Color(0xFF059669),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD1FAE5),
        onPrimaryContainer = Color(0xFF022C1E),
        secondary = Color(0xFFD97706),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFEF3C7),
        onSecondaryContainer = Color(0xFF451A03),
        tertiary = Color(0xFF0891B2),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFE0F2FE),
        onTertiaryContainer = Color(0xFF0C4A6E),
        background = Color(0xFFF0FDF4),
        onBackground = Color(0xFF064E3B),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF064E3B),
        surfaceVariant = Color(0xFFD1E7DD),
        onSurfaceVariant = Color(0xFF335C4D),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFECFDF5),
        surfaceContainer = Color(0xFFD1FAE5),
        surfaceContainerHigh = Color(0xFFA7F3D0),
        surfaceContainerHighest = Color(0xFF6EE7B7),
        outline = Color(0xFF34D399),
        outlineVariant = Color(0xFFA7F3D0)
    )

    // ── 3. Sunset Coral (Velvet Plum & Coral Glow) ────────────────
    val SunsetCoralDark: ColorScheme = darkColorScheme(
        primary = Color(0xFFFF6B6B),            // Coral Accent
        onPrimary = Color(0xFF49000D),
        primaryContainer = Color(0xFF6C0018),
        onPrimaryContainer = Color(0xFFFFD9DF),
        secondary = Color(0xFFFFB800),          // Amber Flare
        onSecondary = Color(0xFF432800),
        secondaryContainer = Color(0xFF623C00),
        onSecondaryContainer = Color(0xFFFFE0B2),
        tertiary = Color(0xFFEC4899),           // Neon Pink
        onTertiary = Color(0xFF4C0028),
        tertiaryContainer = Color(0xFF70003E),
        onTertiaryContainer = Color(0xFFFFD6E8),
        background = Color(0xFF130914),         // Deep Velvet Charcoal
        onBackground = Color(0xFFFDF4F8),
        surface = Color(0xFF1E1021),            // Dark Plum Container
        onSurface = Color(0xFFFDF4F8),
        surfaceVariant = Color(0xFF331D38),
        onSurfaceVariant = Color(0xFFC7A7CD),
        surfaceContainerLowest = Color(0xFF0C050D),
        surfaceContainerLow = Color(0xFF170C19),
        surfaceContainer = Color(0xFF221326),
        surfaceContainerHigh = Color(0xFF2E1B33),
        surfaceContainerHighest = Color(0xFF3B2342),
        outline = Color(0xFF5E3A66),
        outlineVariant = Color(0xFF3D2442)
    )

    val SunsetCoralLight: ColorScheme = lightColorScheme(
        primary = Color(0xFFE53935),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFEBEE),
        onPrimaryContainer = Color(0xFF5C000B),
        secondary = Color(0xFFF57C00),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFE0B2),
        onSecondaryContainer = Color(0xFF4E1D00),
        tertiary = Color(0xFFD81B60),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFCE4EC),
        onTertiaryContainer = Color(0xFF4A001E),
        background = Color(0xFFFFF5F7),
        onBackground = Color(0xFF3D091B),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF3D091B),
        surfaceVariant = Color(0xFFF8BBD0),
        onSurfaceVariant = Color(0xFF5C2D3E),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFFF0F3),
        surfaceContainer = Color(0xFFFFD6E0),
        surfaceContainerHigh = Color(0xFFFFB3C6),
        surfaceContainerHighest = Color(0xFFFF80A0),
        outline = Color(0xFFF48FB1),
        outlineVariant = Color(0xFFFFC1E3)
    )

    // ── 4. Tokyo Glass (Electric Futuristic Glassmorphic) ─────────
    val TokyoGlassDark: ColorScheme = darkColorScheme(
        primary = Color(0xFF00D2FF),            // Electric Cyan Blue
        onPrimary = Color(0xFF003544),
        primaryContainer = Color(0xFF004D62),
        onPrimaryContainer = Color(0xFFC2F3FF),
        secondary = Color(0xFFFF007F),          // Cyber Magenta
        onSecondary = Color(0xFF4D0024),
        secondaryContainer = Color(0xFF700037),
        onSecondaryContainer = Color(0xFFFFB4D0),
        tertiary = Color(0xFF7928CA),           // Deep Neon Purple
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFF4C1D95),
        onTertiaryContainer = Color(0xFFDDD6FE),
        background = Color(0xFF080C14),         // Midnight Tech Obsidian
        onBackground = Color(0xFFF1F5F9),
        surface = Color(0xFF101726),            // Tech Slate Container
        onSurface = Color(0xFFF1F5F9),
        surfaceVariant = Color(0xFF1E293B),
        onSurfaceVariant = Color(0xFF94A3B8),
        surfaceContainerLowest = Color(0xFF05080E),
        surfaceContainerLow = Color(0xFF0D131F),
        surfaceContainer = Color(0xFF151E30),
        surfaceContainerHigh = Color(0xFF1E2A42),
        surfaceContainerHighest = Color(0xFF283754),
        outline = Color(0xFF3B82F6),
        outlineVariant = Color(0xFF1E293B)
    )

    val TokyoGlassLight: ColorScheme = lightColorScheme(
        primary = Color(0xFF0284C7),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFE0F2FE),
        onPrimaryContainer = Color(0xFF0369A1),
        secondary = Color(0xFFC026D3),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFAE8FF),
        onSecondaryContainer = Color(0xFF86198F),
        tertiary = Color(0xFF6D28D9),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFEDE9FE),
        onTertiaryContainer = Color(0xFF5B21B6),
        background = Color(0xFFF8FAFC),
        onBackground = Color(0xFF0F172A),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF0F172A),
        surfaceVariant = Color(0xFFE2E8F0),
        onSurfaceVariant = Color(0xFF475569),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFF1F5F9),
        surfaceContainer = Color(0xFFE2E8F0),
        surfaceContainerHigh = Color(0xFFCBD5E1),
        surfaceContainerHighest = Color(0xFF94A3B8),
        outline = Color(0xFF38BDF8),
        outlineVariant = Color(0xFFBAE6FD)
    )


    // ── 5. 朱塗り Shu-Nuri (Vermilion Lacquer — Miyajima Torii Gate) ─────────
    val ShuNuriDark: ColorScheme = darkColorScheme(
        primary             = Color(0xFFD44000),    // Cinnabar Vermilion — torii pillars
        onPrimary           = Color(0xFF3D0C00),
        primaryContainer    = Color(0xFF7A1F00),
        onPrimaryContainer  = Color(0xFFFFCFBB),
        secondary           = Color(0xFF5B8FA8),    // Steel Sea Blue — calm water
        onSecondary         = Color(0xFF0D2533),
        secondaryContainer  = Color(0xFF1A3D52),
        onSecondaryContainer= Color(0xFFBEDFEF),
        tertiary            = Color(0xFFB0D4E8),    // Powder Sky — open horizon
        onTertiary          = Color(0xFF0C2233),
        tertiaryContainer   = Color(0xFF1A3A4D),
        onTertiaryContainer = Color(0xFFD6ECF7),
        background          = Color(0xFF0C0A08),    // Deep charcoal-slate, warm dark
        onBackground        = Color(0xFFF5EEE8),
        surface             = Color(0xFF13100E),    // Dark wood/lacquer surface
        onSurface           = Color(0xFFF5EEE8),
        surfaceVariant      = Color(0xFF2A1F18),
        onSurfaceVariant    = Color(0xFFC4A99A),
        surfaceContainerLowest  = Color(0xFF080605),
        surfaceContainerLow     = Color(0xFF100D0B),
        surfaceContainer        = Color(0xFF1E1915),
        surfaceContainerHigh    = Color(0xFF29211C),
        surfaceContainerHighest = Color(0xFF352A23),
        surfaceBright       = Color(0xFF3D302A),
        surfaceDim          = Color(0xFF0A0806),
        outline             = Color(0xFF4A3020),    // Dark terracotta
        outlineVariant      = Color(0xFF2E1E12)
    )

    val ShuNuriLight: ColorScheme = lightColorScheme(
        primary             = Color(0xFFC0340A),    // Rich vermilion
        onPrimary           = Color(0xFFFFFFFF),
        primaryContainer    = Color(0xFFFFCFBB),
        onPrimaryContainer  = Color(0xFF3D0C00),
        secondary           = Color(0xFF4A7A94),    // Deep sea blue
        onSecondary         = Color(0xFFFFFFFF),
        secondaryContainer  = Color(0xFFD0EAF5),
        onSecondaryContainer= Color(0xFF0D2533),
        tertiary            = Color(0xFF4F7D9A),    // Mountain mist
        onTertiary          = Color(0xFFFFFFFF),
        tertiaryContainer   = Color(0xFFD6ECF7),
        onTertiaryContainer = Color(0xFF0C2233),
        background          = Color(0xFFF5F0EC),    // Warm rice paper
        onBackground        = Color(0xFF1E1209),
        surface             = Color(0xFFFDFAF7),    // Washi paper white
        onSurface           = Color(0xFF1E1209),
        surfaceVariant      = Color(0xFFEEE4DC),
        onSurfaceVariant    = Color(0xFF5C3E30),
        surfaceContainerLowest  = Color(0xFFFFFFFF),
        surfaceContainerLow     = Color(0xFFF8F3EF),
        surfaceContainer        = Color(0xFFF0E9E3),
        surfaceContainerHigh    = Color(0xFFE8DDD5),
        surfaceContainerHighest = Color(0xFFE0D0C6),
        outline             = Color(0xFF8A6550),
        outlineVariant      = Color(0xFFD4B9A8)
    )

    // ── 6. 御神酒 O-Miki (Sacred Sake — Meiji Kazaridaru Barrels) ─────────────
    val OMikiDark: ColorScheme = darkColorScheme(
        primary             = Color(0xFFDC143C),    // Bold crimson — red barrel bands
        onPrimary           = Color(0xFF42000F),
        primaryContainer    = Color(0xFF70001E),
        onPrimaryContainer  = Color(0xFFFFB3C0),
        secondary           = Color(0xFF4CAF7D),    // Shrine forest green
        onSecondary         = Color(0xFF00331C),
        secondaryContainer  = Color(0xFF004D2B),
        onSecondaryContainer= Color(0xFFA8F0C6),
        tertiary            = Color(0xFFD4A853),    // Golden straw/rope
        onTertiary          = Color(0xFF3A2500),
        tertiaryContainer   = Color(0xFF593A00),
        onTertiaryContainer = Color(0xFFFFDFA0),
        background          = Color(0xFF0E0808),    // Deep espresso
        onBackground        = Color(0xFFF7EDEA),
        surface             = Color(0xFF160D0D),    // Dark lacquered wood
        onSurface           = Color(0xFFF7EDEA),
        surfaceVariant      = Color(0xFF2C1515),
        onSurfaceVariant    = Color(0xFFD4A5A5),
        surfaceContainerLowest  = Color(0xFF090505),
        surfaceContainerLow     = Color(0xFF120A0A),
        surfaceContainer        = Color(0xFF1C1010),
        surfaceContainerHigh    = Color(0xFF261616),
        surfaceContainerHighest = Color(0xFF311D1D),
        surfaceBright       = Color(0xFF3D2525),
        surfaceDim          = Color(0xFF0B0606),
        outline             = Color(0xFF5C2020),    // Dark crimson border
        outlineVariant      = Color(0xFF3A1515)
    )

    val OMikiLight: ColorScheme = lightColorScheme(
        primary             = Color(0xFFC4122E),    // Deep crimson
        onPrimary           = Color(0xFFFFFFFF),
        primaryContainer    = Color(0xFFFFD6DC),
        onPrimaryContainer  = Color(0xFF42000F),
        secondary           = Color(0xFF1E5C3C),    // Dark shrine green
        onSecondary         = Color(0xFFFFFFFF),
        secondaryContainer  = Color(0xFFB8EDD0),
        onSecondaryContainer= Color(0xFF00331C),
        tertiary            = Color(0xFF9A6E0A),    // Warm gold
        onTertiary          = Color(0xFFFFFFFF),
        tertiaryContainer   = Color(0xFFFFE0A0),
        onTertiaryContainer = Color(0xFF3A2500),
        background          = Color(0xFFFFF8F0),    // Warm ivory cream — barrel canvas
        onBackground        = Color(0xFF1E0808),
        surface             = Color(0xFFFFFCF8),    // Pure ivory
        onSurface           = Color(0xFF1E0808),
        surfaceVariant      = Color(0xFFEFDDD8),
        onSurfaceVariant    = Color(0xFF5C2828),
        surfaceContainerLowest  = Color(0xFFFFFFFF),
        surfaceContainerLow     = Color(0xFFFFF5F0),
        surfaceContainer        = Color(0xFFFFEDE5),
        surfaceContainerHigh    = Color(0xFFF5DDD5),
        surfaceContainerHighest = Color(0xFFEBCEC5),
        outline             = Color(0xFF9A4040),
        outlineVariant      = Color(0xFFDDB0A8)
    )

    // ── 7. Neo-Brutalist Poster (FC88 High-Energy Editorial Mint & Chalk) ───
    val NeonBrutalistDark: ColorScheme = darkColorScheme(
        primary             = Color(0xFF00E676),    // High-energy Electric Mint
        onPrimary           = Color(0xFF003819),
        primaryContainer    = Color(0xFF004D25),
        onPrimaryContainer  = Color(0xFFB9FFD4),
        secondary           = Color(0xFFFF1744),    // Vibrant Coral Flame Accent
        onSecondary         = Color(0xFF38000A),
        secondaryContainer  = Color(0xFF5C0012),
        onSecondaryContainer= Color(0xFFFFB4AB),
        tertiary            = Color(0xFF00F2FE),    // Electric Cyan Pulse
        onTertiary          = Color(0xFF00363A),
        tertiaryContainer   = Color(0xFF004F56),
        onTertiaryContainer = Color(0xFFB1F5FF),
        background          = Color(0xFF0F1412),    // Deep Chalkboard Slate
        onBackground        = Color(0xFFE2EBE5),
        surface             = Color(0xFF161D1A),    // Brutalist Poster Surface
        onSurface           = Color(0xFFE2EBE5),
        surfaceVariant      = Color(0xFF222B27),
        onSurfaceVariant    = Color(0xFFA0B3A8),
        surfaceContainerLowest  = Color(0xFF0A0E0D),
        surfaceContainerLow     = Color(0xFF1B2420),
        surfaceContainer        = Color(0xFF202A26),
        surfaceContainerHigh    = Color(0xFF28342E),
        surfaceContainerHighest = Color(0xFF303E38),
        outline             = Color(0xFF00E676),
        outlineVariant      = Color(0xFF334A3E)
    )

    val NeonBrutalistLight: ColorScheme = lightColorScheme(
        primary             = Color(0xFF00A852),
        onPrimary           = Color(0xFFFFFFFF),
        primaryContainer    = Color(0xFFB9FFD4),
        onPrimaryContainer  = Color(0xFF00210B),
        secondary           = Color(0xFFD50000),
        onSecondary         = Color(0xFFFFFFFF),
        secondaryContainer  = Color(0xFFFFDAD6),
        onSecondaryContainer= Color(0xFF410002),
        tertiary            = Color(0xFF00838F),
        onTertiary          = Color(0xFFFFFFFF),
        tertiaryContainer   = Color(0xFFE0F7FA),
        onTertiaryContainer = Color(0xFF00272B),
        background          = Color(0xFFF4F8F5),
        onBackground        = Color(0xFF0C1611),
        surface             = Color(0xFFFFFFFF),
        onSurface           = Color(0xFF0C1611),
        surfaceVariant      = Color(0xFFDAE6DE),
        onSurfaceVariant    = Color(0xFF3F4D45),
        surfaceContainerLowest  = Color(0xFFFFFFFF),
        surfaceContainerLow     = Color(0xFFEEF5F0),
        surfaceContainer        = Color(0xFFE6EFE8),
        surfaceContainerHigh    = Color(0xFFDDE7E0),
        surfaceContainerHighest = Color(0xFFD4DEC7),
        outline             = Color(0xFF00A852),
        outlineVariant      = Color(0xFFBCCBC1)
    )

    // ── 8. 🛕 स्थापत्य Sthapatya (Ancient Indian Temple Architecture) ─────────────
    val SthapatyaDark: ColorScheme = darkColorScheme(
        primary             = Color(0xFFD4AF37),    // Brass Temple Gold — Garbhagriha glow
        onPrimary           = Color(0xFF3B2E00),
        primaryContainer    = Color(0xFF574400),
        onPrimaryContainer  = Color(0xFFFFF0B8),
        secondary           = Color(0xFFC87D55),    // Red Sandstone — Nagara Khajuraho Shikhara
        onSecondary         = Color(0xFF3E1A08),
        secondaryContainer  = Color(0xFF5E2B13),
        onSecondaryContainer= Color(0xFFFFDBCB),
        tertiary            = Color(0xFFE34234),    // Vermillion Sindoor Accent
        onTertiary          = Color(0xFF490600),
        tertiaryContainer   = Color(0xFF731206),
        onTertiaryContainer = Color(0xFFFFDAD4),
        background          = Color(0xFF141210),    // Sculpted Tanjore Granite Dark
        onBackground        = Color(0xFFF0EAE1),
        surface             = Color(0xFF1C1916),    // Dark Charnockite Surface
        onSurface           = Color(0xFFF0EAE1),
        surfaceVariant      = Color(0xFF332B25),
        onSurfaceVariant    = Color(0xFFD8C4B6),
        surfaceContainerLowest  = Color(0xFF0F0D0B),
        surfaceContainerLow     = Color(0xFF191613),
        surfaceContainer        = Color(0xFF221E1A),
        surfaceContainerHigh    = Color(0xFF2D2823),
        surfaceContainerHighest = Color(0xFF38322C),
        surfaceBright       = Color(0xFF423B34),
        surfaceDim          = Color(0xFF12100E),
        outline             = Color(0xFFB8860B),    // Pillar Bronze Outline
        outlineVariant      = Color(0xFF4E3D2A)
    )

    val SthapatyaLight: ColorScheme = lightColorScheme(
        primary             = Color(0xFF8B3A2B),    // Khajuraho Red Sandstone Primary
        onPrimary           = Color(0xFFFFFFFF),
        primaryContainer    = Color(0xFFFFDBCB),
        onPrimaryContainer  = Color(0xFF3B0B03),
        secondary           = Color(0xFF9E6516),    // Carved Ocher Sandstone
        onSecondary         = Color(0xFFFFFFFF),
        secondaryContainer  = Color(0xFFFFDFB3),
        onSecondaryContainer= Color(0xFF341C00),
        tertiary            = Color(0xFFC0340A),    // Vermillion Accent
        onTertiary          = Color(0xFFFFFFFF),
        tertiaryContainer   = Color(0xFFFFDAD4),
        onTertiaryContainer = Color(0xFF410002),
        background          = Color(0xFFFAF4ED),    // Warm Ivory Sandstone Canvas
        onBackground        = Color(0xFF241A15),
        surface             = Color(0xFFFFFBF7),    // Chiseled Sandstone White Surface
        onSurface           = Color(0xFF241A15),
        surfaceVariant      = Color(0xFFF4E5D8),
        onSurfaceVariant    = Color(0xFF6B5548),
        surfaceContainerLowest  = Color(0xFFFFFFFF),
        surfaceContainerLow     = Color(0xFFF6ECE2),
        surfaceContainer        = Color(0xFFEFE2D5),
        surfaceContainerHigh    = Color(0xFFE7D7C7),
        surfaceContainerHighest = Color(0xFFDECCBA),
        outline             = Color(0xFF9C7660),
        outlineVariant      = Color(0xFFD4C1B2)
    )

    fun getColorScheme(flavor: ThemeFlavor, isDark: Boolean): ColorScheme? {
        return null // Clean baseline strip: fall back directly to native M3 dynamic color scheme!
    }
}
