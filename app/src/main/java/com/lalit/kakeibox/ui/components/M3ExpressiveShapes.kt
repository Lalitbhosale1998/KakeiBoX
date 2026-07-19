package com.personal.kakeibox.ui.components

import android.graphics.PointF
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.*

/**
 * A custom Shape that renders a regular polygon with rounded corners.
 * Mapped to M3 shapes like Pentagon, Rounded Triangle, Hexagon, etc.
 */
class RoundedPolygonShape(
    val sides: Int,
    val cornerRadiusPercent: Float = 0.2f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val radius = min(size.width, size.height) / 2f
        val cx = size.width / 2f
        val cy = size.height / 2f
        val points = ArrayList<PointF>()

        // Generate polygon vertices
        for (i in 0 until sides) {
            val angle = (i * 2 * Math.PI / sides) - Math.PI / 2
            val x = cx + radius * cos(angle).toFloat()
            val y = cy + radius * sin(angle).toFloat()
            points.add(PointF(x, y))
        }

        // Draw path with rounded corners using quadratic curves
        val cornerRadius = radius * cornerRadiusPercent
        val sizePoints = points.size

        for (i in 0 until sizePoints) {
            val current = points[i]
            val next = points[(i + 1) % sizePoints]
            val prev = points[(i - 1 + sizePoints) % sizePoints]

            // Prev direction vector
            val toPrevX = prev.x - current.x
            val toPrevY = prev.y - current.y
            val lenPrev = sqrt(toPrevX * toPrevX + toPrevY * toPrevY)
            val nPrevX = toPrevX / lenPrev
            val nPrevY = toPrevY / lenPrev

            // Next direction vector
            val toNextX = next.x - current.x
            val toNextY = next.y - current.y
            val lenNext = sqrt(toNextX * toNextX + toNextY * toNextY)
            val nNextX = toNextX / lenNext
            val nNextY = toNextY / lenNext

            // Start & end points of the rounded corner
            val startX = current.x + nPrevX * cornerRadius
            val startY = current.y + nPrevY * cornerRadius
            val endX = current.x + nNextX * cornerRadius
            val endY = current.y + nNextY * cornerRadius

            if (i == 0) {
                path.moveTo(startX, startY)
            } else {
                path.lineTo(startX, startY)
            }

            // Draw rounded corner quadratic bezier curve
            path.quadraticTo(current.x, current.y, endX, endY)
        }
        path.close()
        return Outline.Generic(path)
    }
}

/**
 * A custom Shape that renders a scalloped circle (Cookie shape).
 * Mapped to M3 shapes like 6-sided cookie, 7-sided cookie, 9-sided cookie, etc.
 */
class CookieShape(
    val petals: Int,
    val scallopDepthPercent: Float = 0.12f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val maxRadius = min(size.width, size.height) / 2f
        val cx = size.width / 2f
        val cy = size.height / 2f
        val steps = 180 // smooth resolution

        val amp = maxRadius * scallopDepthPercent
        val baseRadius = maxRadius - amp

        for (i in 0..steps) {
            val theta = (i * 2 * Math.PI / steps)
            val r = baseRadius + amp * cos(petals * theta).toFloat()
            val x = cx + r * cos(theta).toFloat()
            val y = cy + r * sin(theta).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
        return Outline.Generic(path)
    }
}
