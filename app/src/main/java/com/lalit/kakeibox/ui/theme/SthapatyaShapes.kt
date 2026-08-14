package com.personal.kakeibox.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SthapatyaShapes {
    // Pancharatha 5-Tiered Stepped Corner Shape for Temple Bento Cards
    val PancharathaCardShape = CutCornerShape(
        topStart = 16.dp,
        topEnd = 8.dp,
        bottomEnd = 16.dp,
        bottomStart = 8.dp
    )

    // Padma (8-Petal Sacred Lotus) Scalloped Shape for Action Chips & Badges
    val PadmaChipShape = RoundedCornerShape(22.dp)

    // Small Stepped Chip Shape for Header Badges
    val PancharathaBadgeShape = CutCornerShape(
        topStart = 8.dp,
        topEnd = 4.dp,
        bottomEnd = 8.dp,
        bottomStart = 4.dp
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
