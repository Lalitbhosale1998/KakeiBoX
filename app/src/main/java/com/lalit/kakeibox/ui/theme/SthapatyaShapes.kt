package com.personal.kakeibox.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

object SthapatyaShapes {
    // Authentic Torana (Ogee Temple Arch) Top Crown Shape
    val ToranaArchShape = object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            val w = size.width
            val h = size.height
            val archHeight = with(density) { 18.dp.toPx() }
            val cornerRadius = with(density) { 16.dp.toPx() }

            val path = Path().apply {
                moveTo(0f, h - cornerRadius)
                quadraticTo(0f, h, cornerRadius, h)
                lineTo(w - cornerRadius, h)
                quadraticTo(w, h, w, h - cornerRadius)
                lineTo(w, archHeight + cornerRadius)
                quadraticTo(w, archHeight, w - cornerRadius, archHeight)

                // Torana Ogee Arch top edge (curves up to central peak at w/2, 0f)
                cubicTo(
                    w * 0.75f, archHeight * 0.7f,
                    w * 0.65f, 0f,
                    w * 0.5f, 0f
                )
                cubicTo(
                    w * 0.35f, 0f,
                    w * 0.25f, archHeight * 0.7f,
                    cornerRadius, archHeight
                )

                quadraticTo(0f, archHeight, 0f, archHeight + cornerRadius)
                close()
            }
            return Outline.Generic(path)
        }
    }

    // Pancharatha 5-Tiered Stepped Corner Shape for Temple Bento Cards (Official M3 Expressive Organic Shape)
    val PancharathaCardShape = RoundedCornerShape(
        topStart = 28.dp,
        topEnd = 12.dp,
        bottomEnd = 28.dp,
        bottomStart = 12.dp
    )

    // Padma (8-Petal Sacred Lotus) Scalloped Shape for Action Chips & Badges
    val PadmaChipShape = RoundedCornerShape(22.dp)

    // Small Stepped Chip Shape for Header Badges (Official M3 Expressive Organic Badge)
    val PancharathaBadgeShape = RoundedCornerShape(
        topStart = 14.dp,
        topEnd = 6.dp,
        bottomEnd = 14.dp,
        bottomStart = 6.dp
    )
}

// Mandapa Pillar Border modifier adding fluted carved stone pillar side borders
fun Modifier.mandapaPillarBorder(
    borderColor: Color,
    borderWidth: Dp = 1.5.dp
): Modifier = this.border(
    border = BorderStroke(borderWidth, borderColor),
    shape = SthapatyaShapes.PancharathaCardShape
)
