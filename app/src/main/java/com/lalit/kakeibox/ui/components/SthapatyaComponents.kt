package com.personal.kakeibox.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.kakeibox.ui.theme.SthapatyaShapes

@Composable
fun KonarkSuryaChakraGauge(
    progress: Float = 0.53f,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val outlineColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    val goldAccent = Color(0xFFD4AF37)

    val infiniteTransition = rememberInfiniteTransition(label = "konark_wheel_phase")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "konark_phase"
    )

    Box(
        modifier = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val strokeWidth = 14.dp.toPx()
            val arcSize = size.width - strokeWidth
            val baseRadius = arcSize / 2f
            val startAngleRad = Math.toRadians(135.0)
            val fullSweepAngleRad = Math.toRadians(270.0)

            // 1. Konark Sun Wheel Outer Rim & Spoke Architecture (12 Carved Spokes)
            val spokeCount = 12
            for (s in 0 until spokeCount) {
                val spokeRatio = s.toFloat() / (spokeCount - 1)
                val spokeAngleRad = startAngleRad + spokeRatio * fullSweepAngleRad
                val rInner = baseRadius - 22.dp.toPx()
                val rOuter = baseRadius - 2.dp.toPx()
                val p1 = Offset(center.x + rInner * kotlin.math.cos(spokeAngleRad).toFloat(), center.y + rInner * kotlin.math.sin(spokeAngleRad).toFloat())
                val p2 = Offset(center.x + rOuter * kotlin.math.cos(spokeAngleRad).toFloat(), center.y + rOuter * kotlin.math.sin(spokeAngleRad).toFloat())
                val isActiveSpoke = spokeRatio <= progress
                val spokeColor = if (isActiveSpoke) goldAccent else outlineColor

                // Draw carved spoke shaft
                drawLine(
                    color = spokeColor,
                    start = p1,
                    end = p2,
                    strokeWidth = 3.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
                // Draw spoke carved medallion dot
                drawCircle(color = spokeColor, radius = 3.dp.toPx(), center = p1)
            }

            // 2. Smooth Inactive Track Ring
            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
            drawArc(
                color = outlineColor,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // 3. Active Wavy Progress Arc with Bezier Curves
            val activeSweepAngleRad = Math.toRadians(270.0 * progress)
            val waveAmplitude = 4.5.dp.toPx()
            val numWaves = 4.5
            val steps = 60

            val points = ArrayList<Offset>()
            for (i in 0..steps) {
                val t = i.toFloat() / steps
                val angle = startAngleRad + t * activeSweepAngleRad
                val waveOffset = (kotlin.math.sin(t * numWaves * 2 * Math.PI + wavePhase)).toFloat() * waveAmplitude
                val r = baseRadius + waveOffset
                val x = center.x + r * kotlin.math.cos(angle).toFloat()
                val y = center.y + r * kotlin.math.sin(angle).toFloat()
                points.add(Offset(x, y))
            }

            val activeWavyPath = Path()
            if (points.isNotEmpty()) {
                activeWavyPath.moveTo(points[0].x, points[0].y)
                for (i in 1 until points.size - 1) {
                    val p0 = points[i]
                    val p1 = points[i + 1]
                    val midX = (p0.x + p1.x) / 2f
                    val midY = (p0.y + p1.y) / 2f
                    activeWavyPath.quadraticTo(p0.x, p0.y, midX, midY)
                }
                activeWavyPath.lineTo(points.last().x, points.last().y)
            }

            drawPath(
                path = activeWavyPath,
                brush = Brush.sweepGradient(
                    colors = listOf(goldAccent, primaryColor, secondaryColor, goldAccent)
                ),
                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Inner Hub Center Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "🛕",
                    fontSize = 14.sp
                )
                Text(
                    text = "14 DAYS",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = goldAccent
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "¥5,806,060",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "TILL NEXT PAYDAY",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ToranaHeaderCard(
    modifier: Modifier = Modifier,
    title: String = "VITTA COMMAND LAB",
    badgeText: String = "⚡ LIVE GAUGE",
    content: @Composable ColumnScope.() -> Unit
) {
    val goldAccent = Color(0xFFD4AF37)
    val primaryColor = MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = SthapatyaShapes.ToranaArchShape,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, goldAccent.copy(alpha = 0.5f)),
        tonalElevation = 6.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Torana Crown Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "🛕",
                        fontSize = 16.sp
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = goldAccent
                    )
                }

                Surface(
                    shape = SthapatyaShapes.PadmaChipShape,
                    color = goldAccent.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.6f))
                ) {
                    Text(
                        text = badgeText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = goldAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}
