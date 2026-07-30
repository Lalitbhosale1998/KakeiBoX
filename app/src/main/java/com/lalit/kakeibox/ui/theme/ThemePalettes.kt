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

    fun getColorScheme(flavor: ThemeFlavor, isDark: Boolean): ColorScheme? {
        return when (flavor) {
            ThemeFlavor.DYNAMIC_MATERIAL -> null // Handled by Material You / fallback
            ThemeFlavor.MIDNIGHT_OBSIDIAN -> if (isDark) MidnightObsidianDark else MidnightObsidianLight
            ThemeFlavor.EMERALD_ZEN -> if (isDark) EmeraldZenDark else EmeraldZenLight
            ThemeFlavor.SUNSET_CORAL -> if (isDark) SunsetCoralDark else SunsetCoralLight
            ThemeFlavor.TOKYO_GLASS -> if (isDark) TokyoGlassDark else TokyoGlassLight
        }
    }
}
