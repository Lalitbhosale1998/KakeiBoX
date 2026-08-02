package com.personal.kakeibox.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween

/**
 * Official Google Material 3 Expressive (M3E) Motion Tokens.
 * Standardized easing curves and duration pairs for fluid, flagship Android motion.
 */
object ExpressiveMotion {

    // ── 🌊 Official M3 Expressive Cubic Bezier Easing Curves ───────────────────
    
    /** Emphasized path for major UI transitions, hero card reveals, and layout morphing. */
    val EasingEmphasized = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    /** Emphasized decelerate curve for incoming popups, bottom sheets, and dialog enters. */
    val EasingEmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)

    /** Emphasized accelerate curve for quick dismissals, sheet exits, and menu closes. */
    val EasingEmphasizedAccelerate = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)

    /** Standard expressive curve for color shifts, selection states, and toggle switches. */
    val EasingStandard = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)


    // ── ⏱️ Official M3 Expressive Duration & Spec Pairs ───────────────────────

    /** 500ms Emphasized Motion Spec for hero reveals, page transitions, and card expansions. */
    fun <T> heroRevealSpec(): TweenSpec<T> = tween(durationMillis = 500, easing = EasingEmphasized)

    /** 400ms Emphasized Decelerate Spec for incoming bottom sheets, popups, and dialog enters. */
    fun <T> sheetEnterSpec(): TweenSpec<T> = tween(durationMillis = 400, easing = EasingEmphasizedDecelerate)

    /** 200ms Emphasized Accelerate Spec for quick sheet exits, menu closes, and dismissals. */
    fun <T> sheetExitSpec(): TweenSpec<T> = tween(durationMillis = 200, easing = EasingEmphasizedAccelerate)

    /** 300ms Standard Spec for selection states, toggle switches, and color transitions. */
    fun <T> standardSpec(): TweenSpec<T> = tween(durationMillis = 300, easing = EasingStandard)
}
