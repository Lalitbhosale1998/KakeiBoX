package com.personal.kakeibox.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring

/**
 * Official Google Material 3 Expressive (M3E) Motion Physics.
 * Leverages expressive spring physics for fluid, tactile, flagship Android 17 transitions.
 */
object ExpressiveMotion {

    /** Expressive spring physics spec for hero reveals, page transitions, and card expansions. */
    fun <T> heroRevealSpec(): SpringSpec<T> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    /** Expressive spring spec for incoming bottom sheets, popups, and dialog enters. */
    fun <T> sheetEnterSpec(): SpringSpec<T> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    /** Expressive spring spec for quick sheet exits, menu closes, and dismissals. */
    fun <T> sheetExitSpec(): SpringSpec<T> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )

    /** Standard expressive spring spec for selection states, toggle switches, and color transitions. */
    fun <T> standardSpec(): SpringSpec<T> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
}
