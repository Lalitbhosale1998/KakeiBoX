package com.personal.kakeibox.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Dynamic Monet color extensions mapping UI domains directly to Material3 Expressive color roles.
 * Never hardcodes static hex values, enabling full Monet dynamic extraction on Android 17.
 */
val ColorScheme.spendColor: Color
    @Composable get() = tertiary

val ColorScheme.savingsColor: Color
    @Composable get() = secondary

val ColorScheme.commuteColor: Color
    @Composable get() = primary

val ColorScheme.badgeColor: Color
    @Composable get() = errorContainer

val ColorScheme.onBadgeColor: Color
    @Composable get() = onErrorContainer