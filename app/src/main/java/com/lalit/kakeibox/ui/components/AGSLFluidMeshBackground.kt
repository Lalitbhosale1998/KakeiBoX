package com.personal.kakeibox.ui.components

import android.graphics.RuntimeShader
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb

private const val AGSL_FLUID_MESH_CODE = """
    uniform vec2 uResolution;
    uniform float uTime;
    uniform vec4 uColorPrimary;
    uniform vec4 uColorTertiary;

    half4 main(in vec2 fragCoord) {
        vec2 st = fragCoord / uResolution.xy;
        float wave1 = sin(st.x * 3.1415 + uTime * 0.8) * 0.5 + 0.5;
        float wave2 = cos(st.y * 3.1415 + uTime * 0.6) * 0.5 + 0.5;
        float mixVal = clamp((wave1 + wave2) * 0.5, 0.0, 1.0);
        return mix(uColorPrimary, uColorTertiary, mixVal);
    }
"""

@Composable
fun AGSLFluidMeshBackground(
    modifier: Modifier = Modifier,
    alpha: Float = 0.18f,
    content: @Composable () -> Unit = {}
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    val infiniteTransition = rememberInfiniteTransition(label = "agsl_mesh_time")
    val animTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "agsl_time"
    )

    val runtimeShader = remember {
        try {
            RuntimeShader(AGSL_FLUID_MESH_CODE)
        } catch (_: Exception) {
            null
        }
    }

    Box(modifier = modifier) {
        if (runtimeShader != null) {
            runtimeShader.setFloatUniform("uTime", animTime)
            runtimeShader.setColorUniform(
                "uColorPrimary",
                primaryColor.copy(alpha = alpha).toArgb()
            )
            runtimeShader.setColorUniform(
                "uColorTertiary",
                tertiaryColor.copy(alpha = alpha).toArgb()
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                runtimeShader.setFloatUniform("uResolution", size.width, size.height)
                drawRect(brush = ShaderBrush(runtimeShader))
            }
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = alpha),
                            tertiaryColor.copy(alpha = alpha * 0.5f),
                            Color.Transparent
                        )
                    )
                )
            }
        }
        content()
    }
}
