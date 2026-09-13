package com.personal.kakeibox.ui.theme

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Android 17 Native Pixel Tactile Haptic Feedback Architecture.
 * Leverages Google Pixel hardware vibrator primitives for rich tactile synesthesia.
 */
class PixelHaptics(context: Context) {
    private val vibrator: Vibrator? = try {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator ?: context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    } catch (e: Exception) {
        null
    }

    fun playTick() {
        try {
            vibrator?.takeIf { it.hasVibrator() }?.vibrate(
                VibrationEffect.startComposition()
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 0.4f)
                    .compose()
            )
        } catch (_: Exception) {}
    }

    fun playClick() {
        try {
            vibrator?.takeIf { it.hasVibrator() }?.vibrate(
                VibrationEffect.startComposition()
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_CLICK, 0.8f)
                    .compose()
            )
        } catch (_: Exception) {}
    }

    fun playThud() {
        try {
            vibrator?.takeIf { it.hasVibrator() }?.vibrate(
                VibrationEffect.startComposition()
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1.0f)
                    .compose()
            )
        } catch (_: Exception) {}
    }

    fun playSuccess() {
        try {
            vibrator?.takeIf { it.hasVibrator() }?.vibrate(
                VibrationEffect.startComposition()
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_QUICK_RISE, 0.6f)
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_CLICK, 1.0f)
                    .compose()
            )
        } catch (_: Exception) {}
    }
}

@Composable
fun rememberPixelHaptics(): PixelHaptics {
    val context = LocalContext.current
    return remember(context) { PixelHaptics(context) }
}
